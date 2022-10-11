package com.ak.base.ui.recycler.decorator

import android.content.Context
import com.ak.app_theme.theme.dpToPx

typealias DecorationShouldDrawRule = (position: Int, totalCount: Int) -> Boolean

data class PsDividerItemDecorationSettings(
    val context: Context,
    val shouldDrawRule: DecorationShouldDrawRule = { _, _ -> true },
    private val offsetDp: Offset? = null,
    private val offsetPixels: Offset? = null,
) {
    data class Offset(
        val top: Float? = null,
        val bottom: Float? = null,
        val left: Float? = null,
        val right: Float? = null,
    )

    val topDecorationOffset = offsetPixels?.top?.toInt() ?: offsetDp?.top?.dpToPx(context) ?: 0
    val bottomDecorationOffset = offsetPixels?.bottom?.toInt() ?: offsetDp?.bottom?.dpToPx(context) ?: 0
    val leftDecorationOffset = offsetPixels?.left?.toInt() ?: offsetDp?.left?.dpToPx(context) ?: 0
    val rightDecorationOffset = offsetPixels?.right?.toInt() ?: offsetDp?.right?.dpToPx(context) ?: 0
}
