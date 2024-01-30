package com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.add

import androidx.lifecycle.viewModelScope
import com.ak.feature_tabaccounts_api.interfaces.IAccountsInteractor
import com.ak.feature_tabaccounts_impl.domain.entity.AccountDomainEntity
import com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.BaseManageAccountViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

class AddNewAccountViewModel @Inject constructor(
    private val accountsInteractor: IAccountsInteractor
) : BaseManageAccountViewModel() {

    override fun onManageAccountAction(name: String, login: String, password: String) {
        super.onManageAccountAction(name, login, password)
        viewModelScope.launch {
            val accountToAdd = AccountDomainEntity(name, login, password)
            try {
                accountsInteractor.addNewAccount(accountToAdd)
                successManageLiveData.call()
            } catch (error: Throwable) {
                handleError(error)
            }
        }
    }
}