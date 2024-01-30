package com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ak.feature_tabaccounts_api.interfaces.IAccountsInteractor
import com.ak.feature_tabaccounts_impl.R
import com.ak.feature_tabaccounts_impl.domain.entity.AccountDomainEntity
import com.ak.feature_tabaccounts_impl.domain.entity.mapToDomainEntity
import com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.BaseManageAccountViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

class EditAccountViewModel @Inject constructor(
    private val accountsInteractor: IAccountsInteractor
) : BaseManageAccountViewModel() {

    private var accountEntityForEdit: AccountDomainEntity? = null

    private val accountDataLiveData = MutableLiveData<Triple<String, String, String>>()

    fun subscribeToAccountData(): LiveData<Triple<String, String, String>> = accountDataLiveData

    fun loadAccountData(accountId: Long) {
        viewModelScope.launch {
            val accountEntity = accountsInteractor.getAccountById(accountId).mapToDomainEntity()
            accountEntityForEdit = accountEntity
            accountDataLiveData.value = Triple(
                accountEntity.getAccountName(),
                accountEntity.getAccountLogin(),
                accountEntity.getAccountPassword()
            )
        }
    }

    override fun onManageAccountAction(name: String, login: String, password: String) {
        super.onManageAccountAction(name, login, password)
        if (accountEntityForEdit == null) {
            shortTimeMessageLiveData.value = resourceManager.getString(R.string.unknown_error_message)
            return
        }

        val updatedAccount = accountEntityForEdit?.also {
            it.accountNameValue = name
            it.accountLoginValue = login
            it.accountPasswordValue = password
        } ?: return

        viewModelScope.launch {
            try {
                accountsInteractor.updateAccount(updatedAccount)
                successManageLiveData.call()
            } catch (error: Throwable) {
                handleError(error)
            }
        }
    }
}