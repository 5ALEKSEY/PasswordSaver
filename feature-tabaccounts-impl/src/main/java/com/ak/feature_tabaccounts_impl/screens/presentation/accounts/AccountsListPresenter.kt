package com.ak.feature_tabaccounts_impl.screens.presentation.accounts

import android.util.Log
import com.ak.base.constants.AppConstants
import com.ak.base.presenter.BasePSPresenter
import com.ak.feature_tabaccounts_api.interfaces.AccountFeatureEntity
import com.ak.feature_tabaccounts_api.interfaces.IAccountsInteractor
import com.ak.feature_tabaccounts_impl.di.FeatureTabAccountsComponent
import com.ak.feature_tabaccounts_impl.screens.adapter.AccountItemModel
import com.ak.feature_tabaccounts_impl.screens.logic.IDataBufferManager
import io.reactivex.android.schedulers.AndroidSchedulers
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class AccountsListPresenter @Inject constructor(
    private val accountsInteractor: IAccountsInteractor,
    private val dataBufferManager: IDataBufferManager
) : BasePSPresenter<IAccountsListView>() {

    private var currentAccountId = 0L

    init {
        FeatureTabAccountsComponent.get().inject(this)
    }

    fun loadPasswords() {
        accountsInteractor.getAllAccounts()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                viewState.setLoadingState(true)
                viewState.setEmptyAccountsState(false)
            }
            .subscribe(
                { list ->
                    val listForDisplay = convertFeatureEntitiesList(list)
                    viewState.setLoadingState(false)
                    viewState.displayAccounts(listForDisplay)
                    viewState.setEmptyAccountsState(list.isEmpty())
                    handleListForDisplay(listForDisplay)
                },
                { throwable ->
                    Log.d("de", "dede")
                })
            .let(this::bindDisposable)
    }

    fun onShowAccountActions(accountId: Long) {
        currentAccountId = accountId
        viewState.showAccountActionsDialog()
    }

    // from actions bottom sheet dialog
    fun onCopyAccountLoginAction() {
        getAccountDataAndStartAction {
            dataBufferManager.copyStringData(it.getAccountLogin())
            viewState.showShortTimeMessage("Copied to clipboard")
            currentAccountId = 0L
        }
    }

    fun onCopyAccountPasswordAction() {
        getAccountDataAndStartAction {
            dataBufferManager.copyStringData(it.getAccountPassword())
            viewState.showShortTimeMessage("Copied to clipboard")
            currentAccountId = 0L
        }
    }

    fun onEditAccountAction() {
        viewState.showEditAccountScreen(currentAccountId)
        currentAccountId = 0L
    }

    fun onDeleteAccountAction() {
        accountsInteractor.deleteAccountById(currentAccountId)
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { currentAccountId = 0L }
            .subscribe(
                {
                    viewState.showShortTimeMessage("deleted")
                },
                { throwable ->
                    viewState.showShortTimeMessage(throwable.message ?: "unknown")
                })
            .let(this::bindDisposable)
    }

    private inline fun getAccountDataAndStartAction(crossinline action: (entity: AccountFeatureEntity) -> Unit) {
        accountsInteractor.getAccountById(currentAccountId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { entity ->
                    action(entity)
                },
                { throwable ->
                    Log.d("dddd", "dddd")
                })
            .let(this::bindDisposable)
    }

    private fun handleListForDisplay(listForDisplay: List<AccountItemModel>) {
        if (listForDisplay.size >= AppConstants.TOOLBAR_SCROLL_MIN_PASSWORDS_SIZE) {
            viewState.enableToolbarScrolling()
        } else {
            viewState.disableToolbarScrolling()
        }
    }

    private fun convertFeatureEntitiesList(entitiesList: List<AccountFeatureEntity>): List<AccountItemModel> {
        val resultList = arrayListOf<AccountItemModel>()
        entitiesList.forEach {
            resultList.add(
                    AccountItemModel(
                    it.getAccountId()!!,
                    it.getAccountName(),
                    it.getAccountLogin(),
                    it.getAccountPassword()
                )
            )
        }
        return resultList
    }
}