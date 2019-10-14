package com.ak.passwordsaver.presentation.screens.auth.security.pincode

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.ak.passwordsaver.R

class PincodeContentInputView(context: Context?, attrs: AttributeSet?) :
    LinearLayout(context, attrs) {

    companion object {
        private const val DEFAULT_FILL_CONTENT_SIZE = 4
    }

    private var mFillContentSize = DEFAULT_FILL_CONTENT_SIZE
    private val mInputPincodeViews = arrayListOf<InputPincodeValueView>()
    lateinit var mOnContentFilled: () -> String

    init {
        orientation = HORIZONTAL
    }

    fun setFillContentSize(size: Int) {
        mFillContentSize = size
    }

    fun putPincodeValue(value: String) {
        val inputPincodeView = InputPincodeValueView(context, null)
        val inputPincodeViewSize = resources.getDimensionPixelSize(R.dimen.input_pincode_value_size)
        val params = LayoutParams(inputPincodeViewSize, inputPincodeViewSize)
        params.weight = 1F
        mInputPincodeViews.add(inputPincodeView)
        addView(inputPincodeView, params)
        inputPincodeView.showInputPincodeValue(value)
    }

    fun removeLastPincodeValue() {
        if (mInputPincodeViews.isNotEmpty()) {
            val lastViewIndex = mInputPincodeViews.size - 1
            val inputPincodeValueView = mInputPincodeViews[lastViewIndex]
            removeView(inputPincodeValueView)
            mInputPincodeViews.removeAt(lastViewIndex)
        }
    }
}