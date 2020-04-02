package com.ak.tabpasswords.presentation.passwordmanage.edit

import com.ak.tabpasswords.presentation.passwordmanage.IBaseManagePasswordView

interface IEditPasswordView : IBaseManagePasswordView {
    fun displayPasswordData(passwordName: String, passwordContent: String)
}