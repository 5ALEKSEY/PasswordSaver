package com.ak.passwordsaver.utils.extensions

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
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

fun Context.showToastMessage(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

private fun Context.hideKeyBoard(focusedView: View) {
    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(focusedView.windowToken, 0)
}
