package com.ak.passwordsaver.presentation.screens.auth.security.pincode

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import com.ak.passwordsaver.R
import com.ak.passwordsaver.utils.bindView

class PincodeNumberView(context: Context?, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    var pincodeValue: Int = 0
    private lateinit var mPrimaryText: String
    private lateinit var mSecondaryText: String

    private val mPrimaryTextView: TextView by bindView(R.id.tv_primary_number_text)
    private val mSecondaryTextView: TextView by bindView(R.id.tv_secondary_number_text)

    init {
        val inflater = LayoutInflater.from(context)
        val pincodeView = inflater.inflate(R.layout.layout_pincode_number_view, this, false)
        addView(pincodeView)
        initViewAttributes(attrs)
        initTextContent()
    }

    private fun initViewAttributes(attrs: AttributeSet?) {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PincodeNumberView,
            0, 0
        )

        try {
            pincodeValue = typedArray.getInt(R.styleable.PincodeNumberView_pincodeValue, 0)
            mPrimaryText = typedArray.getString(R.styleable.PincodeNumberView_primaryText) ?: ""
            mSecondaryText = typedArray.getString(R.styleable.PincodeNumberView_secondaryText) ?: ""
        } finally {
            typedArray.recycle()
        }
    }

    private fun initTextContent() {
        mPrimaryTextView.text = mPrimaryText
        mSecondaryTextView.text = mSecondaryText
    }
}