package com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage

import com.ak.base.presenter.BasePSPresenter
import com.ak.feature_tabaccounts_impl.domain.usecase.AccountDataCheckException

abstract class BaseManageAccountPresenter<MV : IBaseManageAccountView> : BasePSPresenter<MV>() {

    abstract fun onManageAccountAction(name: String, login: String, password: String)

    protected fun handleError(throwable: Throwable) {
        when (throwable) {
            is AccountDataCheckException -> {
                dataCheckExceptionHandle(throwable)
            }
            else -> {
                viewState.showShortTimeMessage(throwable.message ?: "")
            }
        }
    }

    private fun dataCheckExceptionHandle(th: AccountDataCheckException) {
        for (field in th.emptyFields) {
            when (field) {
                AccountDataCheckException.ACCOUNT_NAME_FIELD -> viewState.displayAccountNameInputError(
                        "Name can't be empty"
                )
                AccountDataCheckException.ACCOUNT_LOGIN_FIELD -> viewState.displayAccountLoginInputError(
                        "Login can't be empty"
                )
                AccountDataCheckException.ACCOUNT_PASSWORD_FIELD -> viewState.displayAccountPasswordInputError(
                        "Password can't be empty"
                )
            }
        }

        for (field in th.incorrectDataFields) {
            // TODO: incorrect fields handling
        }
    }
}