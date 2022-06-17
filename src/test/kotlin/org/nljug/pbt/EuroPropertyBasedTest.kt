package org.nljug.pbt

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.property.Arb
import io.kotest.property.Shrinker
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.checkAll

class EuroPropertyBasedTest: StringSpec({
    infix fun Euro.shouldBe(other: Euro) =
        this.toCents() shouldBeExactly other.toCents()

    "Sum of Euros is equal to the separate Euros" {
        checkAll(Arb.positiveInt(), Arb.positiveInt()) { a, b ->
            Euro.fromCents(a) shouldBe Euro.fromCents(b)
        }
    }

    fun Arb.Companion.euro() = arbitrary {
        val cents = Arb.positiveInt().bind()
        Euro.fromCents(cents)
    }

    "Sum of positive Euros is equal to the separate Euros (with custom arb)" {
        checkAll(Arb.euro(), Arb.euro()) { a, b ->
            a shouldBe b
        }
    }

    val euroShrinker = Shrinker<Euro> {
        euro -> listOf(
            Euro.fromCents(euro.toCents() + 1),
            Euro.fromCents(euro.toCents() - 1),
            Euro.fromCents(euro.toCents() + 100),
            Euro.fromCents(euro.toCents() - 100),
        )
    }

    fun Arb.Companion.euroWithShrinking() = arbitrary(shrinker = euroShrinker) {
        val cents = Arb.positiveInt().bind()
        Euro.fromCents(cents)
    }

    "Sum of positive Euros is equal to the separate Euros (with custom shrinking arb)" {
        checkAll(Arb.euroWithShrinking(), Arb.euro()) { a, b ->
            a shouldBe b
        }
    }
})