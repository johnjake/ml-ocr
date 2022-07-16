package app.scanner.calc.features

import androidx.lifecycle.viewModelScope
import app.scanner.calc.bases.BaseState
import app.scanner.calc.bases.BaseViewModel
import app.scanner.domain.model.MathData
import app.scanner.domain.repository.ReaderAction
import app.scanner.domain.utils.EMPTY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
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

    fun getResult(readInput: String, imageId: Int) {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, error ->
                showErrorMessage(true, error)
            }
        ) {
            ocrFlow.emit(BaseState.ShowLoader)
            val mathExpression = ocrReader.getExpression(readInput)
            val computed = ocrReader.solveMathEquation(mathExpression)
            ocrFlow.emit(
                BaseState.OnSuccess(
                    MathData(
                        id = imageId,
                        expression = mathExpression,
                        result = computed
                    )
                )
            )
            ocrFlow.emit(BaseState.HideLoader)
        }
    }

    override fun showErrorMessage(withHideLoader: Boolean, errorMsg: Throwable) {
        super.showErrorMessage(withHideLoader, errorMsg)
        emitState {
            if (withHideLoader) {
                ocrFlow.emit(BaseState.HideLoader)
            }
            ocrFlow.emit(
                BaseState.OnFailure(
                    error = errorMsg.message ?: EMPTY
                )
            )
        }
    }
}
