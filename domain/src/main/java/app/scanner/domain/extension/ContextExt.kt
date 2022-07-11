package app.scanner.domain.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import app.scanner.domain.R
import app.scanner.domain.utils.UNKNOWN_TASK
import com.google.android.gms.tasks.Task
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@ColorInt
fun Context.setColor(@ColorRes color: Int): Int {
   return ContextCompat.getColor(this, color)
}

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

suspend fun <T> Task<T>.await(): T = suspendCoroutine { continuation ->
   addOnCompleteListener { task ->
      if (task.isSuccessful) {
         continuation.resume(task.result)
      } else {
         continuation.resumeWithException(task.exception ?: RuntimeException(UNKNOWN_TASK))
      }
   }
}
