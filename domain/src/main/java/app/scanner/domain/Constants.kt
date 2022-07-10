package app.scanner.domain

import android.Manifest

const val EMPTY = ""
const val REQUEST_CODE_PERMISSIONS = 10
val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
const val PATTERN_MATH = "[^0-9()+\\-*/.]"