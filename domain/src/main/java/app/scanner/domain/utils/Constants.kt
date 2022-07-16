package app.scanner.domain.utils

import android.Manifest

const val EMPTY = ""
const val REQUEST_CODE_PERMISSIONS = 10
val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
const val PATTERN_MATH = "[^0-9()+\\-*/.]"
const val OPEN_ALL_FILES = "image/*"
const val INVALID_MATH_EXPRESSION = "Invalid Expression"
const val UNKNOWN_TASK = "Unknown Task Exception"
