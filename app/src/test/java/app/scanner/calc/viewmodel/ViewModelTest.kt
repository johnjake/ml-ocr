package app.scanner.calc.viewmodel

import app.scanner.calc.baserule.CoroutineTestRule
import app.scanner.calc.bases.BaseState
import app.scanner.calc.features.MainViewModel
import app.scanner.domain.extension.roundOff
import app.scanner.domain.extension.verifyContainDecimal
import app.scanner.domain.model.MathData
import app.scanner.domain.repository.ReaderAction
import app.scanner.domain.repository.ReaderRepository
import bsh.Interpreter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@Suppress("DEPRECATION")
@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelTest {
    @get:Rule
    val testRule = CoroutineTestRule()

    @Mock
    private lateinit var repository: ReaderAction

    @Mock lateinit var repositoryImp: ReaderRepository

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = repositoryImp
        viewModel = MainViewModel(repository)
    }

    private fun solverMath(mathExpression: String): String {
        val interpreter = Interpreter()
        interpreter.eval("result =$mathExpression")
        return interpreter["result"].toString()
    }

    @Test

    fun `extract math expression from text`() {
        val expression = "I LOVE YOU4-5+4SD"
        val mathExpression = "4-5+4"
        runBlocking {
            `when`(repository.getExpression(expression)).thenReturn(mathExpression)
            val output = viewModel.getMathExpression(expression)
            println("ViewModel math expression: $output")
            Assert.assertEquals(output, mathExpression)
        }
    }

    @Test
    fun `solve given math expression`() {
        val mathExpression = "5 + 4 - 4"
        val correctAnswer = solverMath(mathExpression)
        runBlocking {
            `when`(repository.solveMathEquation(mathExpression)).thenReturn(correctAnswer)
            val output = viewModel.solveMathEquation(mathExpression)
            println("ViewModel solve math expression: $output")
            Assert.assertEquals(output, correctAnswer)
        }
    }

    @Test
    fun `extract math equation and solve`() {
        val expression = "I LOVE YOU5 + 4 - 4SD"
        val mathExpression = "5 + 4 - 4"
        val correctAnswer = solverMath(mathExpression)
        val data = MathData(id = 1, expression = mathExpression, result = correctAnswer)
        val baseData = BaseState.EquationResult(data)
        runBlocking {
            `when`(repository.getExpression(expression)).thenReturn(mathExpression)
            `when`(repository.solveMathEquation(mathExpression)).thenReturn(correctAnswer)
            val result = viewModel.solveMathEquation(mathExpression)
            println("Computed result: $result")
            println("Equation result: ${baseData.result}")
            Assert.assertEquals(result, correctAnswer)
            Assert.assertEquals(data, baseData.result)
        }
    }

    /** test extension **/

    @Test
    fun `verify if the numbers contain decimal`() {
        val resultVerification = 123.45.verifyContainDecimal()
        println("the given number contains decimal: $resultVerification")
        Assert.assertEquals(resultVerification, true)
    }

    @Test
    fun `number contain decimal round-it-off 2 decimal places otherwise its whole number`() {
        val givenNumDecimal = "15.4778"
        val givenWholeNum = "15"
        val resultDecimal = givenNumDecimal.roundOff()
        val resultWholeNum = givenWholeNum.roundOff()
        println("result contains decimal: $resultDecimal")
        println("result contains whole number: $resultWholeNum")
        Assert.assertEquals(resultDecimal, "15.48")
        Assert.assertEquals(resultWholeNum, "15")
    }
}
