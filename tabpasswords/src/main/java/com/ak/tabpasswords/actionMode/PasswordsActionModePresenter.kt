package com.ak.tabpasswords.actionMode

import com.ak.base.presenter.BasePSPresenter
import com.ak.domain.passwords.IPasswordsInteractor
import com.ak.tabpasswords.di.PasswordsComponentProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class PasswordsActionModePresenter @Inject constructor(
    private val passwordsInteractor: IPasswordsInteractor
) : BasePSPresenter<IPasswordsActionModeView>() {

    private var mSelectedPasswordsIdsList = arrayListOf<Long>()
    private var mIsSelectedModeActive = false

    init {
        (applicationContext as PasswordsComponentProvider)
            .providePasswordsComponent()
            .inject(this)
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
            passwordsInteractor.deletePasswordsByIds(mSelectedPasswordsIdsList)
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