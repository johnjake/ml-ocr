package app.scanner.domain.repository

import app.scanner.domain.utils.CAMERA_SYSTEM
import app.scanner.domain.utils.FILE_SYSTEM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CheckVariant {
    suspend fun getBuildVariant(input: String): String  =
        withContext(Dispatchers.IO) {
            when (input) {
                FILE_SYSTEM -> input
                else -> CAMERA_SYSTEM
            }
        }
}