package com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.edit

import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.IBaseManagePasswordView

interface IEditPasswordView : IBaseManagePasswordView {
    fun displayPasswordData(passwordName: String, passwordContent: String)
}