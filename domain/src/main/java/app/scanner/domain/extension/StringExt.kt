package app.scanner.domain.extension

import android.text.Spanned
import androidx.core.text.HtmlCompat
import app.scanner.domain.utils.ALPHABETICAL_LETTER
import app.scanner.domain.utils.WILDCARD_CHARACTER
import java.util.Locale
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.floor

fun String.spannableCustom(hexColor: String, isBold: Boolean): Spanned {
    return when (isBold) {
        true -> HtmlCompat.fromHtml("<font color='$hexColor' ><b>$this</b></font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
        else -> HtmlCompat.fromHtml("<font color='$hexColor' >$this</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}

fun Number.verifyContainDecimal() = floor(this.toDouble()) != this

private fun verifyDecimalPoint(n: Number) =
    floor(n.toDouble()) != n

fun String.roundOff(): String {
    val random = this.toDouble()
    return when (verifyDecimalPoint(random)) {
        true -> {
            return String.format(Locale.US, "%.2f", random)
        } else -> this
    }
}

fun String.validateWildChar(): Matcher {
    return Pattern.compile(WILDCARD_CHARACTER).matcher(this)
}

fun String.validateAlphaChar(): Matcher {
    return Pattern.compile(ALPHABETICAL_LETTER).matcher(this)
}

fun String.validateDigitOnly(): Boolean {
    return this.all { char -> char.isDigit() }
}

fun String.mathValidation(): Boolean {
    return listOf(
        this.validateDigitOnly(),
        this.validateAlphaChar().find(),
        this.validateWildChar().find()
    ).contains(true).not()
}
