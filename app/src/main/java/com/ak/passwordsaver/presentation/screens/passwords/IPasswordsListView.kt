package com.ak.passwordsaver.presentation.screens.passwords

import com.ak.passwordsaver.presentation.base.IBaseAppView
import com.ak.passwordsaver.presentation.screens.passwords.adapter.PasswordItemModel

interface IPasswordsListView : IBaseAppView {
    fun setLoadingState(isLoading: Boolean)
    fun setEmptyPasswordsState(isEmptyViewVisible: Boolean)
    fun displayPasswords(passwordModelsList: List<PasswordItemModel>)
    fun openPasswordDialogMode(passwordName: String, passwordContent: String)
    fun openPasswordToastMode(passwordName: String, passwordContent: String)
    fun setPasswordVisibilityInCardMode(passwordId: Long, contentVisibilityState: Boolean)
}