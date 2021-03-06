package com.ak.feature_tabpasswords_impl.screens.presentation.passwords

import com.ak.base.ui.IBaseAppView
import com.ak.feature_tabpasswords_impl.screens.adapter.PasswordItemModel

interface IPasswordsListView : IBaseAppView {
    fun setLoadingState(isLoading: Boolean)
    fun setEmptyPasswordsState(isEmptyViewVisible: Boolean)
    fun displayPasswords(passwordModelsList: List<PasswordItemModel>)
    fun openPasswordToastMode(passwordName: String, passwordContent: String)
    fun setPasswordVisibilityInCardMode(passwordId: Long, contentVisibilityState: Boolean)
    fun enableToolbarScrolling()
    fun disableToolbarScrolling()
    fun showPasswordActionsDialog()
    fun hidePasswordActionsDialog()
    fun showEditPasswordScreen(passwordId: Long)
}