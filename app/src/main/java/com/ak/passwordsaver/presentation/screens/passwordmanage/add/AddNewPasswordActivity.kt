package com.ak.passwordsaver.presentation.screens.passwordmanage.add

import android.content.Context
import android.content.Intent
import com.ak.passwordsaver.presentation.screens.passwordmanage.BaseManagePasswordActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class AddNewPasswordActivity : BaseManagePasswordActivity<AddNewPasswordPresenter>(),
    IAddNewPasswordView {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, AddNewPasswordActivity::class.java))
        }
    }

    @InjectPresenter
    lateinit var addNewPasswordPresenter: AddNewPasswordPresenter

    @ProvidePresenter
    fun providePresenter(): AddNewPasswordPresenter = daggerPresenter

    override fun getPresenter() = addNewPasswordPresenter

    override fun getToolbarTitleText() = "Add new password"
}
