package app.scanner.calc.viewmodel

import app.scanner.calc.baserule.CoroutineTestRule
import app.scanner.calc.features.MainViewModel
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
            `when`(repository.solveMathEquation(mathExpression)).thenReturn(mathExpression)
            val output = viewModel.getMathResult(mathExpression)
            println("ViewModel solve math expression: $output")
            Assert.assertEquals(output, correctAnswer)
        }
    }
}