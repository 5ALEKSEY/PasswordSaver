package com.ak.passwordsaver.presentation.screens.passwordmanage.edit

import android.os.Bundle
import androidx.navigation.NavController
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.screens.passwordmanage.BaseManagePasswordFragment
import kotlinx.android.synthetic.main.fragment_manage_password.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.jetbrains.anko.bundleOf

class EditPasswordFragment : BaseManagePasswordFragment<EditPasswordPresenter>(),
    IEditPasswordView {

    companion object {
        private const val PASSWORD_ID_FOR_EDIT_EXTRA_KEY = "password_id_for_edit"

        fun navigateToEditPassword(navController: NavController, passwordId: Long) {
            navController.navigate(
                R.id.action_passwordsListFragment_to_editPasswordFragment,
                bundleOf(PASSWORD_ID_FOR_EDIT_EXTRA_KEY to passwordId)
            )
        }
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
