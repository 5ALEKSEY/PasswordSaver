package com.ak.passwordsaver.model

import android.content.Context
import android.support.annotation.StringRes
import com.ak.passwordsaver.R

enum class PasswordShowingType(val number: Int, @StringRes val messageStringResId: Int) {
    DIALOG(0, R.string.password_showing_dialog_type),
    TOAST(1, R.string.password_showing_toast_type),
    IN_CARD(2, R.string.password_showing_in_card_type);

    companion object {
        fun getTypeByNumber(number: Int) =
            when (number) {
                0 -> PasswordShowingType.DIALOG
                1 -> PasswordShowingType.TOAST
                2 -> PasswordShowingType.IN_CARD
                else -> PasswordShowingType.DIALOG
            }

        fun getListOfTypesMessages(context: Context) =
            listOf(
                context.getString(DIALOG.messageStringResId),
                context.getString(TOAST.messageStringResId),
                context.getString(IN_CARD.messageStringResId)
            )
    }
}