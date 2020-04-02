package com.ak.tabpasswords.navigation.inside

import androidx.navigation.NavController
import com.ak.tabpasswords.navigation.cross.IPasswordsTabCrossModuleNavigator

internal interface IPasswordsTabNavigator {
    fun setupNavigator(
        navigationController: NavController,
        crossModuleNavigator: IPasswordsTabCrossModuleNavigator
    )

    // navigation inside tabpasswords module
    fun navigateToEditPassword(passwordId: Long)
    fun navigateToAddNewPassword()
}