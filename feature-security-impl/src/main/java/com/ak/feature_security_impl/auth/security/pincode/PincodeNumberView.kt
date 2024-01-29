package com.ak.feature_security_impl.auth.security.pincode

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.ak.base.extensions.vibrate
import com.ak.feature_security_impl.R

class PincodeNumberView(context: Context, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs), View.OnClickListener {

    private val tvPrimaryNumberText by lazy { findViewById<TextView>(R.id.tvPrimaryNumberText) }
    private val tvSecondaryNumberText by lazy { findViewById<TextView>(R.id.tvSecondaryNumberText) }

    companion object {
        private const val NUMBER_CLICK_VIBRATION_DELAY_IN_MILLIS = 30L
    }

    private var mPincodeValue: Int = 0
    private lateinit var mPrimaryText: String
    private lateinit var mSecondaryText: String
    private lateinit var mNumberClickListener: (pincodeValue: String) -> Unit


    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.layout_pincode_number_view, this, true)
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
            // todo: fetch primary text and secondary from pincode value enum
            mPincodeValue = typedArray.getInt(R.styleable.PincodeNumberView_pincodeValue, 0)
            mPrimaryText = typedArray.getString(R.styleable.PincodeNumberView_primaryText) ?: ""
            mSecondaryText = typedArray.getString(R.styleable.PincodeNumberView_secondaryText) ?: ""
        } finally {
            typedArray.recycle()
        }
    }

    private fun initTextContent() {
        tvPrimaryNumberText.text = mPrimaryText
        tvSecondaryNumberText.text = mSecondaryText
    }
}