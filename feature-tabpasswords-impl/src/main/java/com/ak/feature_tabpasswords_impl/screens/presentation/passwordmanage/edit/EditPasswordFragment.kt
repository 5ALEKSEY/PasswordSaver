package com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.edit

import android.os.Bundle
import com.ak.base.viewmodel.injectViewModel
import com.ak.feature_tabpasswords_impl.R
import com.ak.feature_tabpasswords_impl.di.FeatureTabPasswordsComponent
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.BaseManagePasswordFragment
import kotlinx.android.synthetic.main.fragment_manage_password.view.tietPasswordContentField
import kotlinx.android.synthetic.main.fragment_manage_password.view.tietPasswordNameField

class EditPasswordFragment : BaseManagePasswordFragment<EditPasswordViewModel>() {

    companion object {
        const val PASSWORD_ID_FOR_EDIT_EXTRA_KEY = "password_id_for_edit"
    }

    override fun getToolbarTitleText() = getString(R.string.add_edit_password_toolbar_title)

    override fun injectFragment(component: FeatureTabPasswordsComponent) {
        component.inject(this)
    }

    override fun createViewModel(): EditPasswordViewModel {
        return injectViewModel(viewModelsFactory)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments?.containsKey(PASSWORD_ID_FOR_EDIT_EXTRA_KEY) == true) {
            viewModel.loadPasswordData(
                requireArguments().getLong(PASSWORD_ID_FOR_EDIT_EXTRA_KEY, 0L)
            )
        } else {
            throw IllegalStateException("Can't open EditPasswordFragment without arguments")
        }
    }

    override fun subscriberToViewModel(viewModel: EditPasswordViewModel) {
        super.subscriberToViewModel(viewModel)
        viewModel.subscribeToPasswordData().observe(viewLifecycleOwner) {
            displayPasswordData(it.first, it.second)
        }
    }

    private fun displayPasswordData(passwordName: String, passwordContent: String) {
        fragmentView.tietPasswordNameField.setText(passwordName)
        fragmentView.tietPasswordContentField.setText(passwordContent)
    }
}
