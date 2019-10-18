package com.ak.passwordsaver.presentation.screens.auth.security.pincode

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.ak.passwordsaver.R
import com.ak.passwordsaver.utils.bindView
import com.ak.passwordsaver.utils.extensions.vibrate

class PincodeNumberView(context: Context?, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs), View.OnClickListener {

    companion object {
        private const val NUMBER_CLICK_VIBRATION_DELAY_IN_MILLIS = 30L
    }

    private var mPincodeValue: Int = 0
    private lateinit var mPrimaryText: String
    private lateinit var mSecondaryText: String
    private lateinit var mNumberClickListener: (pincodeValue: String) -> Unit

    private val mPrimaryTextView: TextView by bindView(R.id.tv_primary_number_text)
    private val mSecondaryTextView: TextView by bindView(R.id.tv_secondary_number_text)

    init {
        val inflater = LayoutInflater.from(context)
        val pincodeView = inflater.inflate(R.layout.layout_pincode_number_view, this, false)
        addView(pincodeView)
        initViewAttributes(attrs)
        initTextContent()
        setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        context?.vibrate(NUMBER_CLICK_VIBRATION_DELAY_IN_MILLIS)
        if (this::mNumberClickListener.isInitialized) {
            mNumberClickListener.invoke(mPincodeValue.toString())
        }
    }

    fun setNumberClickListener(listener: (pincodeValue: String) -> Unit) {
        mNumberClickListener = listener
    }

    private fun initViewAttributes(attrs: AttributeSet?) {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PincodeNumberView,
            0, 0
        )

        try {
            mPincodeValue = typedArray.getInt(R.styleable.PincodeNumberView_pincodeValue, 0)
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