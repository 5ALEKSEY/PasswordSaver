package com.ak.passwordsaver.presentation.screens.addnew

import com.ak.passwordsaver.presentation.base.ui.IBaseAppView

interface IAddNewPasswordView : IBaseAppView {
    fun displaySuccessPasswordSave()
    fun displayPasswordNameInputError(errorMessage: String)
    fun hidePasswordNameInputError()
    fun displayPasswordContentInputError(errorMessage: String)
    fun hidePasswordContentInputError()
    fun displayPasswordAvatarChooserImage(resId: Int)
    fun dismissPasswordAvatarChooserDialog()
}