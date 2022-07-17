package app.scanner.domain.repository

import app.scanner.domain.extension.mathValidation
import app.scanner.domain.extension.roundOff
import app.scanner.domain.utils.EMPTY
import app.scanner.domain.utils.INVALID_MATH_EXPRESSION
import app.scanner.domain.utils.NO_COMPUTED_RESULT
import app.scanner.domain.utils.PATTERN_MATH
import bsh.Interpreter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReaderRepository : ReaderAction {
    override suspend fun getExpression(expression: String): String =
        withContext(Dispatchers.IO) {
            val mathExpression = expression.replace(PATTERN_MATH.toRegex(), EMPTY)
            when {
                mathExpression.mathValidation() -> mathExpression
                else -> INVALID_MATH_EXPRESSION
            }
        }

    override suspend fun solveMathEquation(mathExpression: String): String =
        withContext(Dispatchers.IO) {
            if (mathExpression != INVALID_MATH_EXPRESSION) {
                try {
                    val interpreter = Interpreter()
                    interpreter.eval("result =$mathExpression")
                    val computedResult = interpreter["result"].toString()
                    computedResult.roundOff()
                } catch (ex: Exception) {
                    "0"
                }
            } else NO_COMPUTED_RESULT
        }
}
