package app.scanner.calc.features

import app.scanner.calc.bases.BaseState
import app.scanner.calc.bases.BaseViewModel
import app.scanner.domain.repository.ReaderAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val ocrReader: ReaderAction
) : BaseViewModel() {
    private val ocrFlow: MutableSharedFlow<BaseState> = MutableSharedFlow(replay = 1)
    val ocrState: SharedFlow<BaseState> = ocrFlow

    suspend fun getMathExpression(expression: String): String = ocrReader.getExpression(expression)
    suspend fun solveMathEquation(mathExpression: String): String = ocrReader.solveMathEquation(mathExpression)
}
