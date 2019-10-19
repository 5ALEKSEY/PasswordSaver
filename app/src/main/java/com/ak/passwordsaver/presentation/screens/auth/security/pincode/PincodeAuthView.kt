package com.ak.passwordsaver.presentation.screens.auth.security.pincode

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import com.ak.passwordsaver.R
import com.ak.passwordsaver.utils.bindView
import com.ak.passwordsaver.utils.extensions.vibrate

class PincodeAuthView(context: Context?, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    companion object {
        private const val FAILED_AUTH_VIBRATION_DELAY_IN_MILLIS = 300L
    }

    lateinit var mOnFinishedAction: (resultPincode: String) -> Unit

    private val mPincodeInputView: PincodeContentInputView by bindView(R.id.pciv_pincode_input)
    private val mPincodeNumberView0: PincodeNumberView by bindView(R.id.pnv_pincode_number_0)
    private val mPincodeNumberView1: PincodeNumberView by bindView(R.id.pnv_pincode_number_1)
    private val mPincodeNumberView2: PincodeNumberView by bindView(R.id.pnv_pincode_number_2)
    private val mPincodeNumberView3: PincodeNumberView by bindView(R.id.pnv_pincode_number_3)
    private val mPincodeNumberView4: PincodeNumberView by bindView(R.id.pnv_pincode_number_4)
    private val mPincodeNumberView5: PincodeNumberView by bindView(R.id.pnv_pincode_number_5)
    private val mPincodeNumberView6: PincodeNumberView by bindView(R.id.pnv_pincode_number_6)
    private val mPincodeNumberView7: PincodeNumberView by bindView(R.id.pnv_pincode_number_7)
    private val mPincodeNumberView8: PincodeNumberView by bindView(R.id.pnv_pincode_number_8)
    private val mPincodeNumberView9: PincodeNumberView by bindView(R.id.pnv_pincode_number_9)
    private val mBackspaceActionButton: ImageView by bindView(R.id.iv_backspace_action)

    private var mIsPincodeInputLocked = false

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.layout_pincode_auth_view, this, true)
        initViewListeners()
    }

    fun setPincodeValuesCount(count: Int) {
        mPincodeInputView.setFillContentSize(count)
    }

    fun setFailedAuthViewState() {
        context.vibrate(FAILED_AUTH_VIBRATION_DELAY_IN_MILLIS)
    }

    fun setPincodeInputLockedState(isLocked: Boolean) {
        mIsPincodeInputLocked = isLocked
    }

    fun clearPincodeInput() {
        mPincodeInputView.clearContentInputView()
    }

    private fun initViewListeners() {
        mPincodeNumberView0.setNumberClickListener(this::onPincodeNumberClicked)
        mPincodeNumberView1.setNumberClickListener(this::onPincodeNumberClicked)
        mPincodeNumberView2.setNumberClickListener(this::onPincodeNumberClicked)
        mPincodeNumberView3.setNumberClickListener(this::onPincodeNumberClicked)
        mPincodeNumberView4.setNumberClickListener(this::onPincodeNumberClicked)
        mPincodeNumberView5.setNumberClickListener(this::onPincodeNumberClicked)
        mPincodeNumberView6.setNumberClickListener(this::onPincodeNumberClicked)
        mPincodeNumberView7.setNumberClickListener(this::onPincodeNumberClicked)
        mPincodeNumberView8.setNumberClickListener(this::onPincodeNumberClicked)
        mPincodeNumberView9.setNumberClickListener(this::onPincodeNumberClicked)

        mBackspaceActionButton.setOnClickListener { mPincodeInputView.removeLastPincodeValue() }
        mPincodeInputView.mOnContentFilled = { mOnFinishedAction.invoke(it) }
    }

    private fun onPincodeNumberClicked(pincodeValue: String) {
        if (!mIsPincodeInputLocked) {
            mPincodeInputView.putPincodeValue(pincodeValue)
        }
    }
}