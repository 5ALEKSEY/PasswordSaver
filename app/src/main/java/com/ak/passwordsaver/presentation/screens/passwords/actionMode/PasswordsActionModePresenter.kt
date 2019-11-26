package com.ak.passwordsaver.presentation.screens.passwords.actionMode

import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.presentation.base.BasePSPresenter
import com.ak.passwordsaver.presentation.screens.passwords.logic.PasswordsListInteractor
import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@InjectViewState
class PasswordsActionModePresenter : BasePSPresenter<IPasswordsActionModeView>() {

    @Inject
    lateinit var mPasswordsListInteractor: PasswordsListInteractor

    private var mSelectedPasswordsIdsList = arrayListOf<Long>()
    private var mIsSelectedModeActive = false

    init {
        PSApplication.appInstance.getApplicationComponent().inject(this)
    }

    fun onPasswordItemLongClick(passwordId: Long) {
        viewState.displaySelectedMode()
        mIsSelectedModeActive = true
        handleNewItemSelection(passwordId)
    }

    fun onPasswordItemSingleClick(passwordId: Long) {
        if (mIsSelectedModeActive) {
            handleNewItemSelection(passwordId)
        }
    }

    private fun handleNewItemSelection(passwordId: Long) {
        val isWasSelected = mSelectedPasswordsIdsList.contains(passwordId)

        if (isWasSelected) {
            mSelectedPasswordsIdsList.remove(passwordId)
            if (mSelectedPasswordsIdsList.isEmpty()) {
                viewState.showSelectStateForItem(!isWasSelected, passwordId)
                viewState.hideSelectedMode()
                return
            }
        } else {
            mSelectedPasswordsIdsList.add(passwordId)
        }

        viewState.showSelectStateForItem(!isWasSelected, passwordId)
        viewState.showSelectedItemsQuantityText(getActionModeTitleText())
    }

    fun onSelectedModeFinished() {
        for (passwordId in mSelectedPasswordsIdsList) {
            viewState.showSelectStateForItem(false, passwordId)
        }
        mIsSelectedModeActive = false
        mSelectedPasswordsIdsList.clear()
    }

    fun onDeleteSelectedInActionMode() {
        if (mSelectedPasswordsIdsList.isNotEmpty()) {
            mPasswordsListInteractor.deletePasswordsById(mSelectedPasswordsIdsList)
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

    private fun getActionModeTitleText() = mSelectedPasswordsIdsList.size.toString()
}