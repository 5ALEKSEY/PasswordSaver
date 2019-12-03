package com.ak.passwordsaver.presentation.screens.passwordmanage.edit

import com.ak.passwordsaver.presentation.screens.passwordmanage.IBaseManagePasswordView

interface IEditPasswordView : IBaseManagePasswordView {
    fun displayPasswordData(passwordName: String, passwordContent: String)
}