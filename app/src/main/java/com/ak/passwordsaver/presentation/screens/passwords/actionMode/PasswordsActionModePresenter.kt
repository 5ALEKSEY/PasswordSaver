package com.ak.passwordsaver.presentation.screens.passwords.actionMode

import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.model.db.PSDatabase
import com.ak.passwordsaver.model.db.entities.PasswordDBEntity
import com.ak.passwordsaver.presentation.base.BasePSPresenter
import com.arellomobile.mvp.InjectViewState
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class PasswordsActionModePresenter : BasePSPresenter<IPasswordsActionModeView>() {

    @Inject
    lateinit var mDatabase: PSDatabase

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

    fun onDeleteAction() {
        if (!mSelectedPasswordsIdsList.isEmpty()) {
            Observable.fromIterable(mSelectedPasswordsIdsList)
                .map { passwordId ->
                    PasswordDBEntity(passwordId)
                }
                .toList()
                .flatMap(::getDeletePasswordsSingle)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        viewState.hideSelectedMode()
                        viewState.showShortTimeMessage("deleted")
                    },
                    { throwable ->
                        viewState.showShortTimeMessage(throwable.message ?: "unknown")
                    })
                .let(::bindDisposable)
        }
    }

    private fun getDeletePasswordsSingle(list: List<PasswordDBEntity>) =
        Single.fromCallable { mDatabase.getPasswordsDao().deletePasswords(*list.toTypedArray()) }

    private fun getActionModeTitleText() = mSelectedPasswordsIdsList.size.toString()
}