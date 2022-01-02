package com.ak.base.utils

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import com.ak.base.R

object PSUtils {
    fun getAbbreviationFormName(name: String): String {
        val list = name.split(" ")
        return when {
            list.isEmpty() -> ""
            list.size > 1 -> list[0].take(1) + list[1].take(1)
            list.size == 1 -> list[0].take(1)
            else -> ""
        }.toUpperCase()
    }

    fun getHidedContentText(
        context: Context,
        isContentVisible: Boolean,
        hidedContent: String
    ): CharSequence {
        return if (isContentVisible) {
            hidedContent
        } else {
            SpannableString(hidedContent).apply {
                hidedContent.forEachIndexed { index, _ ->
                    val imageSpan = ImageSpan(
                        context,
                        R.drawable.hided_element,
                        ImageSpan.ALIGN_BOTTOM
                    )
                    setSpan(imageSpan, index, index + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                }
            }
        }
    }
}