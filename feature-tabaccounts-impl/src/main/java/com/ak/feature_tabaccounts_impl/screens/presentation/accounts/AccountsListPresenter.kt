package com.ak.feature_tabaccounts_impl.screens.presentation.accounts

import android.util.Log
import com.ak.base.constants.AppConstants
import com.ak.base.presenter.BasePSPresenter
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.feature_tabaccounts_api.interfaces.AccountFeatureEntity
import com.ak.feature_tabaccounts_api.interfaces.IAccountsInteractor
import com.ak.feature_tabaccounts_impl.R
import com.ak.feature_tabaccounts_impl.di.FeatureTabAccountsComponent
import com.ak.feature_tabaccounts_impl.screens.adapter.AccountItemModel
import com.ak.feature_tabaccounts_impl.screens.logic.IDataBufferManager
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject
import moxy.InjectViewState

@InjectViewState
class AccountsListPresenter @Inject constructor(
    private val accountsInteractor: IAccountsInteractor,
    private val dataBufferManager: IDataBufferManager,
    private val resourceManager: IResourceManager
) : BasePSPresenter<IAccountsListView>() {

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

    // from actions bottom sheet dialog
    fun onCopyAccountLoginAction(accountId: Long) {
        getAccountDataAndStartAction(accountId) {
            dataBufferManager.copyStringData(it.getAccountLogin())
            viewState.showShortTimeMessage(resourceManager.getString(R.string.copied_to_clipboard_message))
        }
    }

    fun onCopyAccountPasswordAction(accountId: Long) {
        getAccountDataAndStartAction(accountId) {
            dataBufferManager.copyStringData(it.getAccountPassword())
            viewState.showShortTimeMessage(resourceManager.getString(R.string.copied_to_clipboard_message))
        }
    }

    fun onEditAccountAction(accountId: Long) {
        viewState.showEditAccountScreen(accountId)
    }

    fun onDeleteAccountAction(accountId: Long) {
        accountsInteractor.deleteAccountById(accountId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                },
                { throwable ->
                    viewState.showShortTimeMessage(throwable.message ?: "unknown")
                })
            .let(this::bindDisposable)
    }

    private inline fun getAccountDataAndStartAction(accountId: Long, crossinline action: (entity: AccountFeatureEntity) -> Unit) {
        accountsInteractor.getAccountById(accountId)
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