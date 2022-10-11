package com.ak.base.extensions

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.hideKeyBoard() {
    currentFocus?.let {
        this.hideKeyBoard(it)
    }
}

fun Fragment.hideKeyboard() {
    activity?.hideKeyBoard()
}

private fun Context.hideKeyBoard(focusedView: View) {
    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(focusedView.windowToken, 0)
}

fun Context.vibrate(vibrateDuration: Long = 200L) {
    (this.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator)?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            it.vibrate(
                VibrationEffect.createOneShot(
                    vibrateDuration,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            @Suppress("DEPRECATION")
            it.vibrate(vibrateDuration)
        }
    }
}

fun FragmentActivity.vibrate(vibrateDuration: Long = 200L) {
    baseContext.vibrate(vibrateDuration)
}

fun Fragment.vibrate(vibrateDuration: Long = 200L) {
    activity?.vibrate(vibrateDuration)
}

fun FragmentActivity.showToastMessage(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Fragment.showToastMessage(message: String, duration: Int = Toast.LENGTH_SHORT) {
    activity?.showToastMessage(message, duration)
}

fun Context.getDrawableCompat(@DrawableRes drawableRes: Int) = ContextCompat.getDrawable(this, drawableRes)

fun Context.getColorCompat(@ColorRes colorRes: Int) = ContextCompat.getColor(this, colorRes)

fun Context.getFontCompat(@FontRes fontRes: Int) = ResourcesCompat.getFont(this, fontRes)

fun FragmentActivity.getColorCompat(@ColorRes colorRes: Int) = baseContext.getColorCompat(colorRes)

fun Fragment.getColorCompat(@ColorRes colorRes: Int) = activity?.getColorCompat(colorRes) ?: 0

fun Fragment.getFontCompat(@FontRes fontRes: Int) = activity?.getFontCompat(fontRes) ?: 0
