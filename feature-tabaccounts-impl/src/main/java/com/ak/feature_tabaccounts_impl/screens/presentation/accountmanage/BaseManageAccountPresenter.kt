package com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage

import com.ak.base.presenter.BasePSPresenter
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.feature_tabaccounts_impl.R
import com.ak.feature_tabaccounts_impl.domain.usecase.AccountDataCheckException
import javax.inject.Inject

abstract class BaseManageAccountPresenter<MV : IBaseManageAccountView> : BasePSPresenter<MV>() {

    @Inject
    protected lateinit var resourceManager: IResourceManager

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
                        resourceManager.getString(
                                R.string.empty_error_message,
                                resourceManager.getString(R.string.name_field_name)
                        )
                )
                AccountDataCheckException.ACCOUNT_LOGIN_FIELD -> viewState.displayAccountLoginInputError(
                        resourceManager.getString(
                                R.string.empty_error_message,
                                resourceManager.getString(R.string.login_field_name)
                        )
                )
                AccountDataCheckException.ACCOUNT_PASSWORD_FIELD -> viewState.displayAccountPasswordInputError(
                        resourceManager.getString(
                                R.string.empty_error_message,
                                resourceManager.getString(R.string.password_field_name)
                        )
                )
            }
        }

        for (field in th.incorrectDataFields) {
            // TODO: incorrect fields handling
        }
    }
}