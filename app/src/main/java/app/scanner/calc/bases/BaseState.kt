package app.scanner.calc.bases

import app.scanner.domain.model.MathData
import app.scanner.domain.utils.EMPTY
import com.google.mlkit.vision.text.Text

sealed class BaseState {
    data class EquationResult(val result: MathData): BaseState()
    data class VariantState(val data: String): BaseState()
    data class OnReadFailed(val error: String? = EMPTY) : BaseState()
    data class OnReadSuccess(val data: Text) : BaseState()
}