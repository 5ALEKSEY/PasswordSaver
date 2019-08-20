package com.ak.passwordsaver.presentation.screens.passwords

import com.ak.passwordsaver.presentation.adapter.passwords.PasswordItemModel
import com.ak.passwordsaver.presentation.base.IBaseAppView

interface IPasswordsListView : IBaseAppView {
    fun displayEmptyPasswordsState()
    fun displayPasswords(passwordModelsList: List<PasswordItemModel>)
}