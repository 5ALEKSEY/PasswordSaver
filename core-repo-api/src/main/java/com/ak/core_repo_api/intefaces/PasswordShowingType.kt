package com.ak.core_repo_api.intefaces

import android.content.Context
import androidx.annotation.StringRes
import com.ak.core_repo_api.R

enum class PasswordShowingType(val number: Int, @StringRes val messageStringResId: Int) {
    TOAST(0, R.string.password_showing_toast_type),
    IN_CARD(1, R.string.password_showing_in_card_type);

    companion object {
        fun getTypeByNumber(number: Int) =
            when (number) {
                0 -> TOAST
                1 -> IN_CARD
                else -> TOAST
            }

        fun getListOfTypesMessages(context: Context) =
            listOf(
                context.getString(TOAST.messageStringResId),
                context.getString(IN_CARD.messageStringResId)
            )
    }
}