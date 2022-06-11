package com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.add

import com.ak.feature_tabaccounts_api.interfaces.IAccountsInteractor
import com.ak.feature_tabaccounts_impl.domain.entity.AccountDomainEntity
import com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.BaseManageAccountViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class AddNewAccountViewModel @Inject constructor(
    private val accountsInteractor: IAccountsInteractor
) : BaseManageAccountViewModel() {

    override fun onManageAccountAction(name: String, login: String, password: String) {
        super.onManageAccountAction(name, login, password)
        accountsInteractor.addNewAccount(
            AccountDomainEntity(
                name,
                login,
                password
            )
        )
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