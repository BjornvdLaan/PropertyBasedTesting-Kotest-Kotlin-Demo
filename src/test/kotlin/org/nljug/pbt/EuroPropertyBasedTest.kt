package org.nljug.pbt

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.property.Arb
import io.kotest.property.Shrinker
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.checkAll

class EuroPropertyBasedTest : StringSpec({
    infix fun Euro.shouldBe(other: Euro) =
        this.toCents() shouldBeExactly other.toCents()

    "Sum of Euros is equal to the separate Euros" {
        checkAll(Arb.positiveInt(), Arb.positiveInt()) { a, b ->
            Euro.fromCents(a) + Euro.fromCents(b) shouldBe Euro.fromCents(a + b)
        }
    }

    "Sum of positive Euros is equal to the separate Euros (with custom arb)" {
        val customEuroArb = arbitrary {
            val cents = Arb.positiveInt().bind()
            Euro.fromCents(cents)
        }

        checkAll(customEuroArb, customEuroArb) { a, b ->
            a + b shouldBe Euro.fromCents(a.toCents() + b.toCents())
        }
    }

    val euroEdgeCases = listOf(
        Euro.fromCents(0)
    )

    val euroShrinker = Shrinker<Euro> { euro ->
        listOf(
            Euro.fromCents(euro.toCents() - 1),
            Euro.fromCents(euro.toCents() - 100)
        )
    }

    fun Arb.Companion.euro() = arbitrary(edgecases = euroEdgeCases, shrinker = euroShrinker) {
        val cents = Arb.positiveInt().bind()
        Euro.fromCents(cents)
    }

    "Sum of positive Euros is equal to the separate Euros (with custom shrinking arb)" {
        checkAll(Arb.euro(), Arb.euro()) { a, b ->
            a + b shouldBe Euro.fromCents(a.toCents() + b.toCents())
        }
    }
})