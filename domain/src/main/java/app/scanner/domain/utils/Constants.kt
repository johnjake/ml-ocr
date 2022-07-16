package app.scanner.domain.utils

import android.Manifest

const val EMPTY = ""
const val REQUEST_CODE_PERMISSIONS = 10
val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
const val PATTERN_MATH = "[^0-9()+\\-*/.]"
const val FILE_SYSTEM = "fileSystem"
const val CAMERA_SYSTEM = "cameraSystem"
const val OPEN_ALL_FILES = "image/*"
const val UNKNOWN_RESULT = "Unknown Result"
const val INVALID_MATH_EXPRESSION = "Invalid Expression"
const val UNKNOWN_TASK = "Unknown Task Exception"
const val ROUND_OFF_PATTERN = "#.##"
