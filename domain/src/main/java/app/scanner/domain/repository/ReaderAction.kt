package app.scanner.domain.repository

import android.graphics.Bitmap
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognizer

interface ReaderAction {
    suspend fun readText(recognizer: TextRecognizer, bitmap: Bitmap): Text
    suspend fun getExpression(expression: String): String
    suspend fun solveMathEquation(mathExpression: String): String
    suspend fun processResult(recognizeText: Text): String
}