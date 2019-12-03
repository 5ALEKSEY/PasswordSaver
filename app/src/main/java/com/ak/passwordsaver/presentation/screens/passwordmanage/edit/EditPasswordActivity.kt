package com.ak.passwordsaver.presentation.screens.passwordmanage.edit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.ak.passwordsaver.presentation.screens.passwordmanage.BaseManagePasswordActivity
import com.arellomobile.mvp.presenter.InjectPresenter

class EditPasswordActivity : BaseManagePasswordActivity<EditPasswordPresenter>(),
    IEditPasswordView {

    companion object {
        private const val PASSWORD_ID_FOR_EDIT_EXTRA_KEY = "password_id_for_edit"

        fun startActivity(context: Context, passwordId: Long) {
            Intent(context, EditPasswordActivity::class.java).let {
                it.putExtra(PASSWORD_ID_FOR_EDIT_EXTRA_KEY, passwordId)
                context.startActivity(it)
            }
        }
    }

    @InjectPresenter
    lateinit var mEditPasswordPresenter: EditPasswordPresenter

    override fun getPresenter() = mEditPasswordPresenter

    override fun getToolbarTitleText() = "Edit password"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!intent.hasExtra(PASSWORD_ID_FOR_EDIT_EXTRA_KEY)) {
            finish()
        } else {
            mEditPasswordPresenter.loadPasswordData(
                intent.getLongExtra(PASSWORD_ID_FOR_EDIT_EXTRA_KEY, 0L)
            )
        }
    }

    override fun displayPasswordData(passwordName: String, passwordContent: String) {
        mPasswordNameEditText.setText(passwordName)
        mPasswordContentEditText.setText(passwordContent)
    }
}
