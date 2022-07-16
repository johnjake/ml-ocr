package app.scanner.domain.repository

import com.google.mlkit.vision.text.Text

interface ReaderAction {
    suspend fun getExpression(expression: String): String
    suspend fun solveMathEquation(mathExpression: String): String
    suspend fun processResult(recognizeText: Text): String
}
