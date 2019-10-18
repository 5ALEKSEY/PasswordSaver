package com.ak.passwordsaver.presentation.screens.auth.security.pincode

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.RelativeLayout
import com.ak.passwordsaver.R
import com.ak.passwordsaver.utils.bindView

class PincodeAuthView(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {

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

    init {
        val inflater = LayoutInflater.from(context)
        val pincodeView = inflater.inflate(R.layout.layout_pincode_auth_view, this, false)
        addView(pincodeView)
        initViewListeners()
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
    }

    private fun onPincodeNumberClicked(pincodeValue: String) {
        mPincodeInputView.putPincodeValue(pincodeValue)
    }
}