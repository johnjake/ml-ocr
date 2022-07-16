package app.scanner.calc.bases

import app.scanner.domain.model.MathData
import app.scanner.domain.utils.EMPTY

sealed class BaseState {
    data class EquationResult(val result: MathData) : BaseState()
    data class OnSuccess(val data: String) : BaseState()
    data class OnFailure(val error: String? = EMPTY) : BaseState()
}
