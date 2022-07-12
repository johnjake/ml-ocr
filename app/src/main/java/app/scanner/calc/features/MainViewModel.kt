package app.scanner.calc.features

import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import app.scanner.calc.bases.BaseState
import app.scanner.calc.bases.BaseViewModel
import app.scanner.domain.repository.CheckVariant
import app.scanner.domain.repository.ReaderAction
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognizer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val ocrReader: ReaderAction
) : BaseViewModel() {
    private val ocrFlow: MutableSharedFlow<BaseState> = MutableSharedFlow(replay = 1)
    val ocrState: SharedFlow<BaseState> = ocrFlow

    fun recognizeExpression(recognizer: TextRecognizer, bitmap: Bitmap) {
        viewModelScope.launch {
            ocrReader.readText(recognizer, bitmap).let { text ->
                when {
                    text.text.isEmpty() -> ocrFlow.emit(BaseState.OnReadFailed())
                    else ->  ocrFlow.emit(BaseState.OnReadSuccess(text))
                }
            }
        }
    }

    suspend fun getMathExpression(expression: String): String = ocrReader.getExpression(expression)
    suspend fun getMathResult(mathExpression: String): String = ocrReader.mathConverter(mathExpression)
    suspend fun getProcessResult(recognizeText: Text): String = ocrReader.processResult(recognizeText)
}