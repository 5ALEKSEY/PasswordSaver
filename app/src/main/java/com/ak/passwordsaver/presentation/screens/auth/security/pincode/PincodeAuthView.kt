package com.ak.passwordsaver.presentation.screens.auth.security.pincode

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.ak.passwordsaver.R

class PincodeAuthView(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {

    init {
        val inflater = LayoutInflater.from(context)
        val pincodeView = inflater.inflate(R.layout.layout_pincode_auth_view, this, false)
        addView(pincodeView)
    }
}