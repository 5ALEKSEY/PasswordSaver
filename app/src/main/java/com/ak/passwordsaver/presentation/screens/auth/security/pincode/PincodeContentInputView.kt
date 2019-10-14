package com.ak.passwordsaver.presentation.screens.auth.security.pincode

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

class PincodeContentInputView(context: Context?, attrs: AttributeSet?) :
    LinearLayout(context, attrs) {

    companion object {
        private const val DEFAULT_FILL_CONTENT_SIZE = 4
    }

    private var mFillContentSize = DEFAULT_FILL_CONTENT_SIZE

    init {
        orientation = HORIZONTAL
    }

    fun setFillContentSize(size: Int) {
        mFillContentSize = size
    }

    fun putPincodeValue(value: String) {

    }

    fun removeLastPincodeValue() {

    }
}