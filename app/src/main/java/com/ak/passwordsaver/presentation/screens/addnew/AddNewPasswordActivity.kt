package com.ak.passwordsaver.presentation.screens.addnew

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.base.BasePSFragmentActivity
import com.arellomobile.mvp.presenter.InjectPresenter

class AddNewPasswordActivity : BasePSFragmentActivity(), IAddNewPasswordView {

    @InjectPresenter
    lateinit var mAddNewPasswordPresenter: AddNewPasswordPresenter

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, AddNewPasswordActivity::class.java))
        }
    }

    override fun getScreenLayoutResId() = R.layout.activity_add_new_password

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
