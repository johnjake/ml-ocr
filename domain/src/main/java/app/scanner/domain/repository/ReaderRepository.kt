package app.scanner.domain.repository

import android.graphics.Bitmap
import app.scanner.domain.EMPTY
import app.scanner.domain.PATTERN_MATH
import app.scanner.domain.extension.await
import bsh.Interpreter
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognizer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReaderRepository : ReaderAction {
    override suspend fun readText(recognizer: TextRecognizer, bitmap: Bitmap): Text {
        val inputImage = InputImage.fromBitmap(bitmap, 0)
        return recognizer.process(inputImage).await()
    }

    override suspend fun getExpression(expression: String): String =
        withContext(Dispatchers.IO) {
            val mathExpression = expression.replace(PATTERN_MATH.toRegex(), "")
            when {
                mathExpression.isNotEmpty() -> mathExpression
                else -> "Invalid math expression"
            }
        }

    override suspend fun mathConverter(mathExpression: String): String =
        withContext(Dispatchers.IO) {
            try {
                val interpreter = Interpreter()
                interpreter.eval("result =$mathExpression")
                interpreter["result"].toString()
            } catch (ex: Exception) {
                "0"
            }
        }

    override suspend fun processResult(recognizeText: Text): String =
        withContext(Dispatchers.IO) {
            var resultText: String = EMPTY
            for (block in recognizeText.textBlocks) {
                for (line in block.lines) {
                    resultText += line.text + " \n"
                }
                resultText += "\n"
            }
            resultText
        }
}