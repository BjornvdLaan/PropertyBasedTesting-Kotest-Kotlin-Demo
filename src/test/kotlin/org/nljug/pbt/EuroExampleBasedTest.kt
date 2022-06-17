package org.nljug.pbt

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeExactly

class EuroExampleBasedTest: StringSpec({
    infix fun Euro.shouldBe(other: Euro) =
        this.toCents() shouldBeExactly other.toCents()

    "Sum of whole Euros is calculated correctly" {
        val a = Euro.fromCents(100)
        val b = Euro.fromCents(200)

        a + b shouldBe Euro.fromCents(300)
    }

    "Sum of Euros with cents is calculated correctly" {
        val a = Euro.fromCents(135)
        val b = Euro.fromCents(270)

        a + b shouldBe Euro.fromCents(405)
    }

    "An amount of Euro plus zero is equal to that same amount" {
        val a = Euro.fromCents(101)
        val b = Euro.fromCents(0)

        a + b shouldBe Euro.fromCents(101)
    }
})