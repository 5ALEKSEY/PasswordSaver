package com.ak.passwordsaver.presentation.screens.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.base.ui.BasePSFragmentActivity

class SecurityActivity : BasePSFragmentActivity(), ISecurityView {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, SecurityActivity::class.java))
        }
    }

    override fun getScreenLayoutResId() = R.layout.activity_security

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
