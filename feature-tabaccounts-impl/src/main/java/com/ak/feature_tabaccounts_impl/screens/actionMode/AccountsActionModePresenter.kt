package com.ak.feature_tabaccounts_impl.screens.actionMode

import com.ak.base.presenter.BasePSPresenter
import com.ak.feature_tabaccounts_api.interfaces.IAccountsInteractor
import com.ak.feature_tabaccounts_impl.di.FeatureTabAccountsComponent
import io.reactivex.android.schedulers.AndroidSchedulers
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class AccountsActionModePresenter @Inject constructor(
    private val passwordsInteractor: IAccountsInteractor
) : BasePSPresenter<IAccountsActionModeView>() {

    private var selectedPasswordsIdsList = arrayListOf<Long>()
    private var isSelectedModeActive = false

    init {
        FeatureTabAccountsComponent.get().inject(this)
    }

    fun onPasswordItemLongClick(passwordId: Long) {
        viewState.displaySelectedMode()
        isSelectedModeActive = true
        handleNewItemSelection(passwordId)
    }

    fun onPasswordItemSingleClick(passwordId: Long) {
        if (isSelectedModeActive) {
            handleNewItemSelection(passwordId)
        }
    }

    private fun handleNewItemSelection(passwordId: Long) {
        val isWasSelected = selectedPasswordsIdsList.contains(passwordId)

        if (isWasSelected) {
            selectedPasswordsIdsList.remove(passwordId)
            if (selectedPasswordsIdsList.isEmpty()) {
                viewState.showSelectStateForItem(!isWasSelected, passwordId)
                viewState.hideSelectedMode()
                return
            }
        } else {
            selectedPasswordsIdsList.add(passwordId)
        }

        viewState.showSelectStateForItem(!isWasSelected, passwordId)
        viewState.showSelectedItemsQuantityText(getActionModeTitleText())
    }

    fun onSelectedModeFinished() {
        for (passwordId in selectedPasswordsIdsList) {
            viewState.showSelectStateForItem(false, passwordId)
        }
        isSelectedModeActive = false
        selectedPasswordsIdsList.clear()
    }

    fun onDeleteSelectedInActionMode() {
        if (selectedPasswordsIdsList.isNotEmpty()) {
            passwordsInteractor.deleteAccountsByIds(selectedPasswordsIdsList)
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

    private fun getActionModeTitleText() = selectedPasswordsIdsList.size.toString()
}