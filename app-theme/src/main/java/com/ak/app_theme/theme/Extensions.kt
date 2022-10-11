package com.ak.app_theme.theme

import android.content.Context
import android.content.res.Configuration
import android.util.TypedValue

fun Context.isDarkNativeUiMode() = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
    Configuration.UI_MODE_NIGHT_YES -> true
    else -> false
}

fun Float.dpToPx(context: Context? = null): Int {
    return applyDimension(context, this, TypedValue.COMPLEX_UNIT_DIP)
}

private fun applyDimension(context: Context?, value: Float, unit: Int): Int {
    val displayMetrics = context?.resources?.displayMetrics
    return TypedValue.applyDimension(unit, value, displayMetrics).toInt()
}