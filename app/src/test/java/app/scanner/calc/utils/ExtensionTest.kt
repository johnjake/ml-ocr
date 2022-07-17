package app.scanner.calc.utils

import app.scanner.domain.extension.mathValidation
import app.scanner.domain.extension.roundOff
import app.scanner.domain.extension.validateAlphaChar
import app.scanner.domain.extension.validateWildChar
import org.junit.Assert
import org.junit.Test

class ExtensionTest {

    @Test
    fun `math expression validation with incorrect or not valid`() {
        val math = "()234234(4.()(500"
        val result = math.mathValidation()
        println("Math expression: $result")
        Assert.assertEquals(false, result)
    }

    @Test
    fun `math expression validation with valid`() {
        val math = "4+5-2*4"
        val result = math.mathValidation()
        println("Math expression: $result")
        Assert.assertEquals(true, result)
    }

    @Test fun `verify wildcard character in expression`() {
        val math = "()234234(4.()(500"
        val result = math.validateWildChar().find()
        println("Math expression: $result")
        Assert.assertEquals(true, result)
    }

    @Test fun `verify alphabetic character in expression`() {
        val math = "()234234(4.()(500A"
        val result = math.validateAlphaChar().find()
        println("Math expression: $result")
        Assert.assertEquals(true, result)
    }

    @Test fun `verify number or digit in expression`() {
        val math = "()234234(4.()(500A"
        val result = math.validateAlphaChar().find()
        println("Math expression: $result")
        Assert.assertEquals(true, result)
    }

    @Test
    fun `round of to the two decimal points if result is double`() {
        val math = "456.34534656"
        val result = math.roundOff()
        println("Round two decimal places: $result")
        Assert.assertEquals("456.35", result)
    }

    @Test
    fun `computed result is whole number remove decimal point`() {
        val math = "456"
        val result = math.roundOff()
        println("Computed result as whole number: $result")
        Assert.assertEquals("456", result)
    }
}
