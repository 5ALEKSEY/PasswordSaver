package com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.edit

import com.ak.feature_tabaccounts_api.interfaces.AccountFeatureEntity
import com.ak.feature_tabaccounts_api.interfaces.IAccountsInteractor
import com.ak.feature_tabaccounts_impl.R
import com.ak.feature_tabaccounts_impl.di.FeatureTabAccountsComponent
import com.ak.feature_tabaccounts_impl.domain.entity.AccountDomainEntity
import com.ak.feature_tabaccounts_impl.domain.entity.mapToDomainEntity
import com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.BaseManageAccountPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class EditAccountPresenter @Inject constructor(
    private val accountsInteractor: IAccountsInteractor
) : BaseManageAccountPresenter<IEditAccountView>() {

    private var accountEntityForEdit: AccountDomainEntity? = null

    init {
        FeatureTabAccountsComponent.get().inject(this)
    }

    fun loadPasswordData(passwordId: Long) {
        accountsInteractor.getAccountById(passwordId)
            .map(AccountFeatureEntity::mapToDomainEntity)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    { accountEntity ->
                        accountEntityForEdit = accountEntity
                        viewState.displayAccountData(
                                accountEntity.getAccountName(),
                                accountEntity.getAccountLogin(),
                                accountEntity.getAccountPassword()
                        )
                    },
                    { throwable ->
                        viewState.showShortTimeMessage(resourceManager.getString(R.string.unknown_error_message))
                    }
            )
            .let(this::bindDisposable)
    }

    override fun onManageAccountAction(name: String, login: String, password: String) {
        if (accountEntityForEdit == null) {
            viewState.showShortTimeMessage(resourceManager.getString(R.string.unknown_error_message))
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
                            viewState.displaySuccessAccountManageAction()
                        }
                    },
                    { throwable ->
                        handleError(throwable)
                    })
            .let(this::bindDisposable)
    }
}