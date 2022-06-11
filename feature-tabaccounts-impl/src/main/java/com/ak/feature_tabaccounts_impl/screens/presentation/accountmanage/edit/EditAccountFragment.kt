package com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.edit

import android.os.Bundle
import com.ak.base.viewmodel.injectViewModel
import com.ak.feature_tabaccounts_impl.R
import com.ak.feature_tabaccounts_impl.di.FeatureTabAccountsComponent
import com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.BaseManageAccountFragment
import kotlinx.android.synthetic.main.fragment_manage_account.view.tietAccountLoginField
import kotlinx.android.synthetic.main.fragment_manage_account.view.tietAccountNameField
import kotlinx.android.synthetic.main.fragment_manage_account.view.tietAccountPasswordField

class EditAccountFragment : BaseManageAccountFragment<EditAccountViewModel>() {

    companion object {
        const val ACCOUNT_ID_FOR_EDIT_EXTRA_KEY = "account_id_for_edit"
    }

    override fun createViewModel(): EditAccountViewModel {
        return injectViewModel(viewModelsFactory)
    }

    override fun getToolbarTitleText() = getString(R.string.add_edit_account_toolbar_title)

    override fun injectFragment(component: FeatureTabAccountsComponent) {
        component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments?.containsKey(ACCOUNT_ID_FOR_EDIT_EXTRA_KEY) == true) {
            viewModel.loadPasswordData(
                requireArguments().getLong(ACCOUNT_ID_FOR_EDIT_EXTRA_KEY, 0L)
            )
        } else {
            throw IllegalStateException("Can't open EditPasswordFragment without arguments")
        }
    }

    override fun subscriberToViewModel(viewModel: EditAccountViewModel) {
        super.subscriberToViewModel(viewModel)
        viewModel.subscribeToAccountData().observe(viewLifecycleOwner) {
            displayAccountData(it.first, it.second, it.third)
        }
    }

    private fun displayAccountData(accountName: String, accountLogin: String, accountPassword: String) {
        fragmentView.tietAccountNameField.setText(accountName)
        fragmentView.tietAccountLoginField.setText(accountLogin)
        fragmentView.tietAccountPasswordField.setText(accountPassword)
    }
}
