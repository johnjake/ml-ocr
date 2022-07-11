package app.scanner.domain.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import app.scanner.domain.R
import com.google.android.gms.tasks.Task
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@ColorInt
fun Context.setColor(@ColorRes color: Int): Int {
   return ContextCompat.getColor(this, color)
}

fun Context.getBitmap(): Bitmap {
  return BitmapFactory.decodeResource(resources, R.drawable.ic_no_image_avail)
}

suspend fun <T> Task<T>.await(): T = suspendCoroutine { continuation ->
   addOnCompleteListener { task ->
      if (task.isSuccessful) {
         continuation.resume(task.result)
      } else {
         continuation.resumeWithException(task.exception ?: RuntimeException("Unknown task exception"))
      }
   }
}