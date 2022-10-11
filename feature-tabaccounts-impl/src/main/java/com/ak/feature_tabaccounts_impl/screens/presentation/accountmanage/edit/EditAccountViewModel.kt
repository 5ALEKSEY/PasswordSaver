package com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ak.feature_tabaccounts_api.interfaces.AccountFeatureEntity
import com.ak.feature_tabaccounts_api.interfaces.IAccountsInteractor
import com.ak.feature_tabaccounts_impl.R
import com.ak.feature_tabaccounts_impl.domain.entity.AccountDomainEntity
import com.ak.feature_tabaccounts_impl.domain.entity.mapToDomainEntity
import com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.BaseManageAccountViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class EditAccountViewModel @Inject constructor(
    private val accountsInteractor: IAccountsInteractor
) : BaseManageAccountViewModel() {

    private var accountEntityForEdit: AccountDomainEntity? = null

    private val accountDataLiveData = MutableLiveData<Triple<String, String, String>>()

    fun subscribeToAccountData(): LiveData<Triple<String, String, String>> = accountDataLiveData

    fun loadPasswordData(passwordId: Long) {
        accountsInteractor.getAccountById(passwordId)
            .map(AccountFeatureEntity::mapToDomainEntity)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { accountEntity ->
                    accountEntityForEdit = accountEntity
                    accountDataLiveData.value = Triple(
                        accountEntity.getAccountName(),
                        accountEntity.getAccountLogin(),
                        accountEntity.getAccountPassword()
                    )
                },
                { throwable ->
                    shortTimeMessageLiveData.value = resourceManager.getString(R.string.unknown_error_message)
                }
            )
            .let(this::bindDisposable)
    }

    override fun onManageAccountAction(name: String, login: String, password: String) {
        super.onManageAccountAction(name, login, password)
        if (accountEntityForEdit == null) {
            shortTimeMessageLiveData.value = resourceManager.getString(R.string.unknown_error_message)
            return
        }

        val updatedAccount = accountEntityForEdit!!.also {
            it.accountNameValue = name
            it.accountLoginValue = login
            it.accountPasswordValue = password
        }

        accountsInteractor.updateAccount(updatedAccount)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { isSuccess ->
                    if (isSuccess) {
                        successManageLiveData.call()
                    }
                },
                { throwable ->
                    handleError(throwable)
                })
            .let(this::bindDisposable)
    }
}