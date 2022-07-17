package app.scanner.domain.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.TypedValue
import androidx.annotation.ColorInt
import app.scanner.domain.R

@ColorInt
fun Context.getPrimaryColor(): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(android.R.attr.colorPrimary, typedValue, true)
    return typedValue.data
}

fun Context.toHexColor(): String {
    val hexColor = Integer.toHexString(this.getPrimaryColor())
    return "#$hexColor"
}

fun Context.getDefaultBitmap(): Bitmap {
    return BitmapFactory.decodeResource(resources, R.drawable.ic_no_image_avail)
}
