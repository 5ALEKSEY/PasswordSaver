package com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.add

import com.ak.base.viewmodel.injectViewModel
import com.ak.feature_tabaccounts_impl.R
import com.ak.feature_tabaccounts_impl.di.FeatureTabAccountsComponent
import com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.BaseManageAccountFragment

class AddNewAccountFragment : BaseManageAccountFragment<AddNewAccountViewModel>() {

    override fun createViewModel(): AddNewAccountViewModel {
        return injectViewModel(viewModelsFactory)
    }

    override fun getToolbarTitleText() = getString(R.string.add_new_account_toolbar_title)

    override fun injectFragment(component: FeatureTabAccountsComponent) {
        component.inject(this)
    }
}
