package app.scanner.domain.extension

import android.text.Spanned
import androidx.core.text.HtmlCompat

fun String.spannableCustom(hexColor: String, isBold: Boolean): Spanned {
    return when (isBold) {
        true -> HtmlCompat.fromHtml("<font color='$hexColor' ><b>$this</b></font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
        else -> HtmlCompat.fromHtml("<font color='$hexColor' >$this</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}

fun Int.convertToHex() {

}