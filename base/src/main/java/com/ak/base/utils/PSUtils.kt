package com.ak.base.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.DrawableMarginSpan
import android.text.style.ImageSpan
import androidx.annotation.DrawableRes
import com.ak.base.R
import com.ak.base.extensions.dpToPx

object PSUtils {
    fun getAbbreviationFormName(name: String): String {
        val list = name.split(" ")
        return when {
            list.isEmpty() -> ""
            list.size > 1 -> list[0].take(1) + list[1].take(1)
            list.size == 1 -> list[0].take(1)
            else -> ""
        }.uppercase()
    }

    fun getHiddenContentText(
        context: Context,
        isContentVisible: Boolean,
        hidedContent: String,
        @DrawableRes hiddenElementRes: Int,
    ): CharSequence {
        return if (isContentVisible) {
            hidedContent
        } else {
            SpannableString(hidedContent).apply {
                hidedContent.forEachIndexed { index, _ ->
                    val imageSpan = ImageSpan(
                        context,
                        hiddenElementRes,
                        ImageSpan.ALIGN_BOTTOM,
                    )
                    setSpan(imageSpan, index, index + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                }
            }
        }
    }

    fun getHiddenContentTextTemp(
        isContentVisible: Boolean,
        hidedContent: String,
        hiddenElement: Drawable,
    ): CharSequence {
        return if (isContentVisible) {
            hidedContent
        } else {
            SpannableString(hidedContent).apply {
                hidedContent.forEachIndexed { index, _ ->
                    val imageSpan = ImageSpan(hiddenElement, ImageSpan.ALIGN_BOTTOM)
                    setSpan(imageSpan, index, index + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                }
            }
        }
    }
}