package com.ak.feature_tabaccounts_impl.screens.actionMode

import com.ak.base.presenter.BasePSPresenter
import com.ak.feature_tabaccounts_api.interfaces.IAccountsInteractor
import com.ak.feature_tabaccounts_impl.di.FeatureTabAccountsComponent
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject
import moxy.InjectViewState

@InjectViewState
class AccountsActionModePresenter @Inject constructor(
    private val passwordsInteractor: IAccountsInteractor
) : BasePSPresenter<IAccountsActionModeView>() {

    private var selectedAccountsIdsList = arrayListOf<Long>()
    private var isSelectedModeActive = false

    init {
        FeatureTabAccountsComponent.get().inject(this)
    }

    fun onAccountItemSelect(accountId: Long) {
        if (!isSelectedModeActive) {
            viewState.displaySelectedMode()
            isSelectedModeActive = true
        }

        handleNewItemSelection(accountId)
    }

    private fun handleNewItemSelection(accountId: Long) {
        val isWasSelected = selectedAccountsIdsList.contains(accountId)

        if (isWasSelected) {
            selectedAccountsIdsList.remove(accountId)
            if (selectedAccountsIdsList.isEmpty()) {
                viewState.showSelectStateForItem(!isWasSelected, accountId)
                viewState.hideSelectedMode()
                return
            }
        } else {
            selectedAccountsIdsList.add(accountId)
        }

        viewState.showSelectStateForItem(!isWasSelected, accountId)
        viewState.showSelectedItemsQuantityText(getActionModeTitleText())
    }

    fun onSelectedModeFinished() {
        for (passwordId in selectedAccountsIdsList) {
            viewState.showSelectStateForItem(false, passwordId)
        }
        isSelectedModeActive = false
        selectedAccountsIdsList.clear()
    }

    fun onDeleteSelectedInActionMode() {
        if (selectedAccountsIdsList.isNotEmpty()) {
            passwordsInteractor.deleteAccountsByIds(selectedAccountsIdsList)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        viewState.hideSelectedMode()
                    },
                    { throwable ->
                        viewState.showShortTimeMessage(throwable.message ?: "unknown")
                    })
                .let(this::bindDisposable)
        }
    }

    private fun getActionModeTitleText() = selectedAccountsIdsList.size.toString()
}