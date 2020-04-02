package com.ak.tabpasswords.presentation.passwordmanage.edit

import android.os.Bundle
import com.ak.tabpasswords.presentation.passwordmanage.BaseManagePasswordFragment
import kotlinx.android.synthetic.main.fragment_manage_password.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class EditPasswordFragment : BaseManagePasswordFragment<EditPasswordPresenter>(),
    IEditPasswordView {

    companion object {
        const val PASSWORD_ID_FOR_EDIT_EXTRA_KEY = "password_id_for_edit"
    }

    @InjectPresenter
    lateinit var editPasswordPresenter: EditPasswordPresenter

    @ProvidePresenter
    fun providePresenter(): EditPasswordPresenter = daggerPresenter

    override fun getPresenter() = editPasswordPresenter

    override fun getToolbarTitleText() = "Edit password"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments?.containsKey(PASSWORD_ID_FOR_EDIT_EXTRA_KEY) == true) {
            editPasswordPresenter.loadPasswordData(
                arguments!!.getLong(PASSWORD_ID_FOR_EDIT_EXTRA_KEY, 0L)
            )
        } else {
            throw IllegalStateException("Can't open EditPasswordFragment without arguments")
        }
    }

    override fun displayPasswordData(passwordName: String, passwordContent: String) {
        tietPasswordNameField.setText(passwordName)
        tietPasswordContentField.setText(passwordContent)
    }
}
