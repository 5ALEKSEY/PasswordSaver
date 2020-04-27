package com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.edit

import com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.IBaseManageAccountView

interface IEditAccountView : IBaseManageAccountView {
    fun displayAccountData(accountName: String, accountLogin: String, accountPassword: String)
}