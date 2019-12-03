package com.ak.passwordsaver.presentation.screens.passwordmanage.add

import android.content.Context
import android.content.Intent
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.screens.passwordmanage.BaseManagePasswordActivity
import com.arellomobile.mvp.presenter.InjectPresenter


class AddNewPasswordActivity : BaseManagePasswordActivity<AddNewPasswordPresenter>(),
    IAddNewPasswordView {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, AddNewPasswordActivity::class.java))
        }
    }

    @InjectPresenter
    lateinit var mAddNewPasswordPresenter: AddNewPasswordPresenter

    override fun getPresenter() = mAddNewPasswordPresenter

    override fun getToolbarTitleText() = "Add new password"
}
