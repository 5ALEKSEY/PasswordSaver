package com.ak.passwordsaver.presentation.screens.passwords

import com.ak.passwordsaver.presentation.screens.passwords.adapter.PasswordItemModel
import com.ak.passwordsaver.presentation.base.IBaseAppView

interface IPasswordsListView : IBaseAppView {
    fun displayEmptyPasswordsState()
    fun displayPasswords(passwordModelsList: List<PasswordItemModel>)
    fun openPasswordForUser(passwordId: Long)
}