package com.ak.feature_tabpasswords_impl.screens.navigation.inside

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.ak.feature_tabpasswords_impl.R
import com.ak.feature_tabpasswords_impl.screens.navigation.cross.IPasswordsTabCrossModuleNavigator
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.edit.EditPasswordFragment
import javax.inject.Inject

internal class PasswordsTabNavigatorImpl @Inject constructor() : IPasswordsTabNavigator {

    private lateinit var crossModuleNavigator: IPasswordsTabCrossModuleNavigator
    private lateinit var passwordsModuleNavigator: NavController

    override fun setupNavigator(
        navigationController: NavController,
        crossModuleNavigator: IPasswordsTabCrossModuleNavigator
    ) {
        this.crossModuleNavigator = crossModuleNavigator
        this.passwordsModuleNavigator = navigationController
    }

    override fun navigateToEditPassword(passwordId: Long) {
        passwordsModuleNavigator.navigate(
            R.id.action_passwordsListFragment_to_editPasswordFragment,
            bundleOf(EditPasswordFragment.PASSWORD_ID_FOR_EDIT_EXTRA_KEY to passwordId)
        )
    }

    override fun navigateToAddNewPassword() {
        passwordsModuleNavigator.navigate(R.id.action_passwordsListFragment_to_addNewPasswordFragment)
    }
}