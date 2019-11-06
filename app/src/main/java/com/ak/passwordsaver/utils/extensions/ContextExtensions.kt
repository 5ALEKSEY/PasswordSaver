package com.ak.passwordsaver.utils.extensions

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.annotation.ColorRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast


fun FragmentActivity.hideKeyBoard() {
    currentFocus?.let {
        this.hideKeyBoard(it)
    }
}

fun Fragment.hideKeyboard() {
    activity?.hideKeyBoard()
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
            //deprecated in API 26
            it.vibrate(500)
        }
    }
}

fun Context.showToastMessage(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Context.getColorCompat(@ColorRes colorRes: Int) =
    ContextCompat.getColor(this, colorRes)

private fun Context.hideKeyBoard(focusedView: View) {
    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(focusedView.windowToken, 0)
}
