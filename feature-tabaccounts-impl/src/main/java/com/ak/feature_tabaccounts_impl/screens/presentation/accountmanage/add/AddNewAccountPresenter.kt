package com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.add

import com.ak.feature_tabaccounts_api.interfaces.IAccountsInteractor
import com.ak.feature_tabaccounts_impl.di.FeatureTabAccountsComponent
import com.ak.feature_tabaccounts_impl.domain.entity.AccountDomainEntity
import com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.BaseManageAccountPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class AddNewAccountPresenter @Inject constructor(
    private val accountsInteractor: IAccountsInteractor
) : BaseManageAccountPresenter<IAddNewAccountView>() {

    init {
        FeatureTabAccountsComponent.get().inject(this)
    }

    override fun onManageAccountAction(name: String, login: String, password: String) {
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
                        viewState.displaySuccessAccountManageAction()
                    }
                },
                { throwable ->
                    handleError(throwable)
                })
            .let(this::bindDisposable)
    }
}