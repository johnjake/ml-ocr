package app.scanner.domain.extension

import android.text.Spanned
import androidx.core.text.HtmlCompat
import timber.log.Timber
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import kotlin.math.floor

fun String.spannableCustom(hexColor: String, isBold: Boolean): Spanned {
    return when (isBold) {
        true -> HtmlCompat.fromHtml("<font color='$hexColor' ><b>$this</b></font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
        else -> HtmlCompat.fromHtml("<font color='$hexColor' >$this</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}

private fun verifyDecimalPoint(n: Number)
   = floor(n.toDouble()) != n

fun String.roundOff(): String {
    val random = this.toDouble()
    return when(verifyDecimalPoint(random)) {
        true -> {
            return String.format(Locale.US, "%.2f", random)
        } else -> this
    }
}