package com.ak.passwordsaver.auth.security.pincode

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.ak.base.extensions.vibrate
import com.ak.passwordsaver.R
import kotlinx.android.synthetic.main.layout_pincode_auth_view.view.*

class PincodeAuthView(context: Context?, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    companion object {
        private const val FAILED_AUTH_VIBRATION_DELAY_IN_MILLIS = 300L
    }

    lateinit var mOnFinishedAction: (resultPincode: String) -> Unit

    private var mIsPincodeInputLocked = false

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.layout_pincode_auth_view, this, true)
        initViewListeners()
    }

    fun setPincodeValuesCount(count: Int) {
        cPincodeInputView.setFillContentSize(count)
    }

    fun setFailedAuthViewState() {
        context.vibrate(FAILED_AUTH_VIBRATION_DELAY_IN_MILLIS)
    }

    fun setPincodeInputLockedState(isLocked: Boolean) {
        mIsPincodeInputLocked = isLocked
    }

    fun clearPincodeInput() {
        cPincodeInputView.clearContentInputView()
    }

    private fun initViewListeners() {
        cPincodeNumberView0.setNumberClickListener(this::onPincodeNumberClicked)
        cPincodeNumberView1.setNumberClickListener(this::onPincodeNumberClicked)
        cPincodeNumberView2.setNumberClickListener(this::onPincodeNumberClicked)
        cPincodeNumberView3.setNumberClickListener(this::onPincodeNumberClicked)
        cPincodeNumberView4.setNumberClickListener(this::onPincodeNumberClicked)
        cPincodeNumberView5.setNumberClickListener(this::onPincodeNumberClicked)
        cPincodeNumberView6.setNumberClickListener(this::onPincodeNumberClicked)
        cPincodeNumberView7.setNumberClickListener(this::onPincodeNumberClicked)
        cPincodeNumberView8.setNumberClickListener(this::onPincodeNumberClicked)
        cPincodeNumberView9.setNumberClickListener(this::onPincodeNumberClicked)

        ivBackspaceAction.setOnClickListener { cPincodeInputView.removeLastPincodeValue() }
        cPincodeInputView.mOnContentFilled = { mOnFinishedAction.invoke(it) }
    }

    private fun onPincodeNumberClicked(pincodeValue: String) {
        if (!mIsPincodeInputLocked) {
            cPincodeInputView.putPincodeValue(pincodeValue)
        }
    }
}