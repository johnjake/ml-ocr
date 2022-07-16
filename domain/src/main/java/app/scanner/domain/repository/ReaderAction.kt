package app.scanner.domain.repository

interface ReaderAction {
    suspend fun getExpression(expression: String): String
    suspend fun solveMathEquation(mathExpression: String): String
}
