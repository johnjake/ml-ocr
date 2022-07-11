package app.scanner.domain

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast


fun Activity.toast(message: String, duration: Int? = null) {
    runOnUiThread { Toast.makeText(this, message, duration ?: Toast.LENGTH_SHORT).show() }
}

fun getActivity(context: Context): Activity? {
    return when (context) {
        null -> null
        is ContextWrapper -> if (context !is Activity) {
                getActivity((context as ContextWrapper).baseContext)
            } else {
                context
            }
        else -> null
    }
}