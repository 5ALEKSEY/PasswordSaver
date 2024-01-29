package com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.edit

import android.os.Bundle
import android.view.View
import com.ak.base.viewmodel.injectViewModel
import com.ak.feature_tabaccounts_impl.R
import com.ak.feature_tabaccounts_impl.di.FeatureTabAccountsComponent
import com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.BaseManageAccountFragment
import com.google.android.material.textfield.TextInputEditText

class EditAccountFragment : BaseManageAccountFragment<EditAccountViewModel>() {

    private var tietAccountLoginField: TextInputEditText? = null
    private var tietAccountNameField: TextInputEditText? = null
    private var tietAccountPasswordField: TextInputEditText? = null

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

    override fun findViews(fragmentView: View) {
        super.findViews(fragmentView)
        with(fragmentView) {
            tietAccountLoginField = findViewById(R.id.tietAccountLoginField)
            tietAccountNameField = findViewById(R.id.tietAccountNameField)
            tietAccountPasswordField= findViewById(R.id.tietAccountPasswordField)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments?.containsKey(ACCOUNT_ID_FOR_EDIT_EXTRA_KEY) == true) {
            viewModel.loadAccountData(
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
        tietAccountNameField?.setText(accountName)
        tietAccountLoginField?.setText(accountLogin)
        tietAccountPasswordField?.setText(accountPassword)
    }
}
