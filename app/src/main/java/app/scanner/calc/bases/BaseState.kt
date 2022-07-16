package app.scanner.calc.bases

import app.scanner.domain.model.MathData
import app.scanner.domain.utils.EMPTY

sealed class BaseState {
    object ShowLoader : BaseState()
    object HideLoader : BaseState()
    data class OnSuccess(val data: MathData) : BaseState()
    data class OnFailure(val error: String? = EMPTY) : BaseState()
}
