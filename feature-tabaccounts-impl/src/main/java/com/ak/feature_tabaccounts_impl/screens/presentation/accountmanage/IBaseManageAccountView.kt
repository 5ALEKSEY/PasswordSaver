package com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage

import com.ak.base.ui.IBaseAppView

interface IBaseManageAccountView : IBaseAppView {
    fun displaySuccessAccountManageAction()
    fun displayAccountNameInputError(errorMessage: String)
    fun hideAccountNameInputError()
    fun displayAccountLoginInputError(errorMessage: String)
    fun hideAccountLoginInputError()
    fun displayAccountPasswordInputError(errorMessage: String)
    fun hideAccountPasswordInputError()
}