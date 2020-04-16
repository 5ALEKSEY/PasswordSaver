package com.ak.feature_tabpasswords_impl.screens.navigation.inside

import androidx.navigation.NavController
import com.ak.feature_tabpasswords_impl.screens.navigation.cross.IPasswordsTabCrossModuleNavigator

internal interface IPasswordsTabNavigator {
    fun setupNavigator(
        navigationController: NavController,
        crossModuleNavigator: IPasswordsTabCrossModuleNavigator
    )

    // navigation inside tabpasswords module
    fun navigateToEditPassword(passwordId: Long)
    fun navigateToAddNewPassword()
}