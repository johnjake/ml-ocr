package app.scanner.domain.extension

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun ImageView.getBitmap(): Bitmap {
    this.invalidate()
    return this.drawable.toBitmap()
}
