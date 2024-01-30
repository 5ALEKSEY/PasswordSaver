package com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage

import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ak.base.livedata.SingleEventLiveData
import com.ak.base.viewmodel.BasePSViewModel
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.feature_tabaccounts_impl.R
import com.ak.feature_tabaccounts_impl.domain.usecase.AccountDataCheckException
import javax.inject.Inject

abstract class BaseManageAccountViewModel : BasePSViewModel() {

    @Inject
    protected lateinit var resourceManager: IResourceManager

    protected val nameInputErrorLiveData = MutableLiveData<String?>()
    protected val loginInputErrorLiveData = MutableLiveData<String?>()
    protected val passwordInputErrorLiveData = MutableLiveData<String?>()
    protected val successManageLiveData = SingleEventLiveData<Unit?>()

    fun subscribeToNameInputError(): LiveData<String?> = nameInputErrorLiveData
    fun subscribeToLoginInputError(): LiveData<String?> = loginInputErrorLiveData
    fun subscribeToPasswordInputError(): LiveData<String?> = passwordInputErrorLiveData
    fun subscribeToSuccessAccountManage(): LiveData<Unit?> = successManageLiveData

    @CallSuper
    open fun onManageAccountAction(name: String, login: String, password: String) {
        nameInputErrorLiveData.value = null
        loginInputErrorLiveData.value = null
        passwordInputErrorLiveData.value = null
    }

    protected fun handleError(throwable: Throwable) {
        when (throwable) {
            is AccountDataCheckException -> {
                dataCheckExceptionHandle(throwable)
            }
            else -> {
                shortTimeMessageLiveData.value = throwable.message ?: ""
            }
        }
    }

    private fun dataCheckExceptionHandle(th: AccountDataCheckException) {
        for (field in th.emptyFields) {
            when (field) {
                AccountDataCheckException.ACCOUNT_NAME_FIELD -> nameInputErrorLiveData.value = (
                    resourceManager.getString(
                        R.string.empty_error_message,
                        resourceManager.getString(R.string.name_field_name)
                    )
                )
                AccountDataCheckException.ACCOUNT_LOGIN_FIELD -> loginInputErrorLiveData.value = (
                    resourceManager.getString(
                        R.string.empty_error_message,
                        resourceManager.getString(R.string.login_field_name)
                    )
                )
                AccountDataCheckException.ACCOUNT_PASSWORD_FIELD -> passwordInputErrorLiveData.value = (
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