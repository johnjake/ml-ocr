package app.scanner.calc.bases

sealed class BaseState {
    data class VariantState(val data: String): BaseState()
}