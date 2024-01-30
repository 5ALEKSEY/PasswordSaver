package com.ak.feature_tabaccounts_impl.screens.navigation.inside

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.ak.feature_tabaccounts_impl.R
import com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.edit.EditAccountFragment
import javax.inject.Inject

internal class AccountsTabNavigatorImpl @Inject constructor() : IAccountsTabNavigator {

    private lateinit var passwordsModuleNavigator: NavController

    override fun setupNavigator(navigationController: NavController) {
        this.passwordsModuleNavigator = navigationController
    }

    override fun navigateToEditAccount(accountId: Long) {
        passwordsModuleNavigator.navigate(
                R.id.action_accountsListFragment_to_editAccountFragment,
                bundleOf(EditAccountFragment.ACCOUNT_ID_FOR_EDIT_EXTRA_KEY to accountId)
        )
    }

    override fun navigateToAddNewAccount() {
        passwordsModuleNavigator.navigate(R.id.action_accountsListFragment_to_addNewAccountFragment)
    }
}