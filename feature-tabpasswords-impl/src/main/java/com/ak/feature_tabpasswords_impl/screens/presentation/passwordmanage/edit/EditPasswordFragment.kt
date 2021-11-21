package com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.edit

import android.os.Bundle
import com.ak.feature_tabpasswords_impl.R
import com.ak.feature_tabpasswords_impl.di.FeatureTabPasswordsComponent
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.BaseManagePasswordFragment
import kotlinx.android.synthetic.main.fragment_manage_password.view.*
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

    override fun getToolbarTitleText() = getString(R.string.add_edit_password_toolbar_title)

    override fun injectFragment() {
        FeatureTabPasswordsComponent.get().inject(this)
    }

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
        fragmentView.tietPasswordNameField.setText(passwordName)
        fragmentView.tietPasswordContentField.setText(passwordContent)
    }
}
