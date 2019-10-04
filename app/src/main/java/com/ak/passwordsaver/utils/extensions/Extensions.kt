package com.ak.passwordsaver.utils.extensions

import android.util.TypedValue
import com.ak.passwordsaver.PSApplication

fun Float.dpToPx(): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        PSApplication.appInstance.resources.displayMetrics
    ).toInt()
}