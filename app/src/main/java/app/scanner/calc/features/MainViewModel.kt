package app.scanner.calc.features

import androidx.lifecycle.viewModelScope
import app.scanner.calc.bases.BaseState
import app.scanner.calc.bases.BaseViewModel
import app.scanner.domain.repository.CheckVariant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val check: CheckVariant
) : BaseViewModel() {
    private val shareFlow: MutableSharedFlow<BaseState> = MutableSharedFlow(replay = 1)
    val mainState: SharedFlow<BaseState> = shareFlow

    fun verifyFlavors(variant: String) {
        viewModelScope.launch {
            val flavor = check.getBuildVariant(variant)
            shareFlow.emit(BaseState.VariantState(flavor))
        }
    }
}