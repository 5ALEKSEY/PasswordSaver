package com.ak.feature_security_impl.auth.security.pincode

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

class PincodeContentInputView(context: Context?, attrs: AttributeSet?) :
    LinearLayout(context, attrs) {

    companion object {
        private const val DEFAULT_FILL_CONTENT_SIZE = 10
        private const val SET_SECRET_NUMBER_STATE_DELAY_IN_MILLIS = 1000L
    }

    private var mFillContentSize =
        DEFAULT_FILL_CONTENT_SIZE
    private val mInputPincodeViews = arrayListOf<InputPincodeValueView>()
    private val mResultPincodeStringBuilder = StringBuilder()
    private val mSecretPincodeStateAction = Runnable(this::setSecretImageForLastPincodeValue)

    lateinit var mOnContentFilled: (resultCode: String) -> Unit

    init {
        orientation = HORIZONTAL
    }

    fun setFillContentSize(size: Int) {
        mFillContentSize = size
    }

    fun clearContentInputView() {
        removeAllViewsInLayout()
        mResultPincodeStringBuilder.clear()
        mInputPincodeViews.clear()
    }

    fun putPincodeValue(value: String) {
        val inputPincodeView =
            InputPincodeValueView(
                context,
                null
            )
        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        params.weight = 1F
        addView(inputPincodeView, params)
        inputPincodeView.showInputPincodeValue(value)
        mResultPincodeStringBuilder.append(value)
        removeCallbacks(mSecretPincodeStateAction)
        setSecretImageForLastPincodeValue()
        mInputPincodeViews.add(inputPincodeView)
        postDelayed(mSecretPincodeStateAction,
            SET_SECRET_NUMBER_STATE_DELAY_IN_MILLIS
        )


        if (mInputPincodeViews.size == mFillContentSize) {
            if (this::mOnContentFilled.isInitialized) {
                mOnContentFilled.invoke(mResultPincodeStringBuilder.toString())
            }
            clearContentInputView()
        }
    }

    fun removeLastPincodeValue() {
        getLastPincodeValueView()?.let {
            val lastViewIndex = mInputPincodeViews.size - 1
            removeView(it)
            mInputPincodeViews.removeAt(lastViewIndex)
            mResultPincodeStringBuilder.deleteCharAt(mResultPincodeStringBuilder.length - 1)
        }
    }

    private fun setSecretImageForLastPincodeValue() {
        getLastPincodeValueView()?.setSecretStateForPincodeValue()
    }

    private fun getLastPincodeValueView() =
        if (mInputPincodeViews.isNotEmpty()) {
            val lastViewIndex = mInputPincodeViews.size - 1
            mInputPincodeViews[lastViewIndex]
        } else {
            null
        }
}