package app.scanner.domain.model

import app.scanner.domain.EMPTY

data class MathData(
    val id: Int? = 0,
    val expression: String? = EMPTY,
    val result: String? = EMPTY
)