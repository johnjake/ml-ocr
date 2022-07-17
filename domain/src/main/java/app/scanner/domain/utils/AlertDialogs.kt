package app.scanner.domain.utils

import android.content.Context
import app.scanner.domain.R
import app.scanner.domain.extension.spannableCustom
import app.scanner.domain.extension.toHexColor
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun showClearDialog(
    context: Context,
    title: String,
    message: String,
    isCancellable: Boolean = false,
    hasNegativeButton: Boolean = false,
    positiveButtonText: String = context.resources.getString(R.string.ok),
    negativeButtonText: String = context.resources.getString(R.string.cancel),
    positiveButtonClicked: (() -> Unit)? = null,
    negativeButtonClicked: (() -> Unit)? = null
) {
    val dialogBuilder = MaterialAlertDialogBuilder(context)

    if (hasNegativeButton) {
        dialogBuilder
            .setNegativeButton(
                negativeButtonText.spannableCustom(
                    hexColor = context.toHexColor(),
                    isBold = false
                )
            ) { dialogInterface, _ ->
                negativeButtonClicked?.invoke()
                dialogInterface.dismiss()
            }
    }

    dialogBuilder
        .setTitle(title)
        .setMessage(
            message.spannableCustom(
                hexColor = context.toHexColor(),
                isBold = false
            )
        )
        .setPositiveButton(
            positiveButtonText.spannableCustom(
                hexColor = context.toHexColor(),
                isBold = false
            )
        ) { dialogInterface, _ ->
            positiveButtonClicked?.invoke()
            dialogInterface.dismiss()
        }
        .setCancelable(isCancellable)
        .show()
}
