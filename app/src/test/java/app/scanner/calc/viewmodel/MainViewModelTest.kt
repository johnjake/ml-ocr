package app.scanner.calc.viewmodel

import app.scanner.calc.baserule.CoroutineTestRule
import app.scanner.calc.bases.BaseState
import app.scanner.calc.features.MainViewModel
import app.scanner.domain.extension.mathValidation
import app.scanner.domain.model.MathData
import app.scanner.domain.repository.ReaderAction
import app.scanner.domain.repository.ReaderRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@Suppress("DEPRECATION")
class MainViewModelTest {
    @get:Rule
    val testRule = CoroutineTestRule()

    @Mock
    private lateinit var repository: ReaderAction

    @Mock
    lateinit var repositoryImp: ReaderRepository

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = repositoryImp
        viewModel = MainViewModel(repository)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `provide expression then extract the math expression and compute`() = testRule.runBlockingTest {
        val expression = "I LOVE YOU4-5+4SD"
        val mathExpression = "4-5+4"
        val correctAnswer = "4"
        val dataList: MutableList<MathData> = arrayListOf()

        val job = launch {
            viewModel.ocrState.collect { state ->
                when (state) {
                    is BaseState.ShowLoader -> println("Show progress bar")
                    is BaseState.HideLoader -> println("Hide progress bar")
                    is BaseState.OnSuccess -> {
                        handleSuccess(state.data)
                        dataList.add(state.data)
                    }
                    is BaseState.OnFailure -> handleFailed(state.error)
                }
            }
        }
        /** mock the repository **/
        `when`(repository.getExpression(expression)).thenReturn(mathExpression)
        `when`(repository.solveMathEquation(mathExpression)).thenReturn(correctAnswer)
        viewModel.getResult(expression, 4)

        Assert.assertEquals("4", dataList[0].result ?: 0)

        /** expected result from println() as follow:
         *  Show Progress bar ->
         * viewModel result ->
         * Hide Progress bar ->
         * **/

        job.cancel()
    }

    private fun handleSuccess(data: MathData) {
        println("data: $data")
    }

    private fun handleFailed(error: String?) {
        println("Error: $error")
    }

    @Test
    fun `math expression validation`() {
        val math = "()234234(4.()(500"
        val result = math.mathValidation()
        println("value result $result")
    }
}
