package com.ak.feature_tabaccounts_impl.screens.navigation.inside

import androidx.navigation.NavController

internal interface IAccountsTabNavigator {
    fun setupNavigator(navigationController: NavController)

    // navigation inside tabaccounts module
    fun navigateToEditAccount(accountId: Long)
    fun navigateToAddNewAccount()
}