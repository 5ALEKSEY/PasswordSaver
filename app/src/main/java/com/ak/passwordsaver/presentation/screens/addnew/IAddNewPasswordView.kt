package com.ak.passwordsaver.presentation.screens.addnew

import com.ak.passwordsaver.presentation.base.IBaseAppView

interface IAddNewPasswordView : IBaseAppView {
    fun displaySuccessPasswordSave()
    fun displayFailedPasswordSave(errorMessage: String)
}