package app.scanner.domain

import android.Manifest

const val EMPTY = ""
const val REQUEST_CODE_PERMISSIONS = 10
const val MAX_SIZE = 1024
const val WIDTH_SIZE = 1080
const val HEIGHT_SIZE = 1080
val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
const val PATTERN_MATH = "[^0-9()+\\-*/.]"