package com.ak.feature_tabpasswords_impl.screens.actionMode

import com.ak.base.presenter.BasePSPresenter
import com.ak.feature_tabpasswords_api.interfaces.IPasswordsInteractor
import com.ak.feature_tabpasswords_impl.di.FeatureTabPasswordsComponent
import io.reactivex.android.schedulers.AndroidSchedulers
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class PasswordsActionModePresenter @Inject constructor(
    private val passwordsInteractor: IPasswordsInteractor
) : BasePSPresenter<IPasswordsActionModeView>() {

    private var selectedPasswordsIdsList = arrayListOf<Long>()
    private var isSelectedModeActive = false

    init {
        FeatureTabPasswordsComponent.get().inject(this)
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
            passwordsInteractor.deletePasswordsByIds(selectedPasswordsIdsList)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        viewState.hideSelectedMode()
                        viewState.showShortTimeMessage("deleted")
                    },
                    { throwable ->
                        viewState.showShortTimeMessage(throwable.message ?: "unknown")
                    })
                .let(this::bindDisposable)
        }
    }

    private fun getActionModeTitleText() = selectedPasswordsIdsList.size.toString()
}