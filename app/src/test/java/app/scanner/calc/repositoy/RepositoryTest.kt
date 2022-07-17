package app.scanner.calc.repositoy

import app.scanner.calc.baserule.CoroutineTestRule
import app.scanner.domain.repository.ReaderAction
import app.scanner.domain.repository.ReaderRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@Suppress("DEPRECATION")
@ExperimentalCoroutinesApi
class RepositoryTest {
    @get:Rule
    val testRule = CoroutineTestRule()

    @Mock
    private lateinit var repository: ReaderAction

    @Mock
    lateinit var repositoryImp: ReaderRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = repositoryImp
    }

    @Test
    fun `get math expression from input string`() = testRule.runBlockingTest {
        val expression = "I LOVE YOU4-5+4SD"
        val mathExpression = "4-5+4"

        `when`(repository.getExpression(expression)).thenReturn(mathExpression)
        val result = repository.getExpression(expression)
        println("Math expression: $result")
        Assert.assertEquals(mathExpression, result)
    }

    @Test
    fun `compute extracted math expression from input string`() = testRule.runBlockingTest {
        val mathExpression = "4-5+4"
        val expectedResult = "4"
        `when`(repository.solveMathEquation(mathExpression)).thenReturn(expectedResult)
        val result = repository.solveMathEquation(mathExpression)
        println("Math expression: $result")
        Assert.assertEquals(expectedResult, result)
    }
}
