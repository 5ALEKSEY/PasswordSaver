package com.ak.feature_tabaccounts_impl.screens.presentation.accounts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ak.base.viewmodel.BasePSViewModel
import com.ak.feature_tabaccounts_api.interfaces.IAccountsInteractor
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class AccountsActionModeViewModel @Inject constructor(
    private val passwordsInteractor: IAccountsInteractor
) : BasePSViewModel() {

    private var selectedAccountsIdsList = arrayListOf<Long>()
    private var isSelectedModeActive = false

    private val selectedModeStateLD = MutableLiveData<Boolean>()
    private val selectedStateForItemLD = MutableLiveData<Pair<Boolean, Long>>()
    private val selectedItemsQuantityTextLD = MutableLiveData<String>()

    fun subscribeToSelectedModeState(): LiveData<Boolean> = selectedModeStateLD
    fun subscribeToSelectedStateForItem(): LiveData<Pair<Boolean, Long>> = selectedStateForItemLD
    fun subscribeToSelectedItemsQuantityText(): LiveData<String> = selectedItemsQuantityTextLD

    fun onAccountItemSelect(accountId: Long) {
        if (!isSelectedModeActive) {
            selectedModeStateLD.value = true
            isSelectedModeActive = true
        }

        handleNewItemSelection(accountId)
    }

    private fun handleNewItemSelection(accountId: Long) {
        val isWasSelected = selectedAccountsIdsList.contains(accountId)

        if (isWasSelected) {
            selectedAccountsIdsList.remove(accountId)
            if (selectedAccountsIdsList.isEmpty()) {
                selectedStateForItemLD.value = !isWasSelected to accountId
                selectedModeStateLD.value = false
                return
            }
        } else {
            selectedAccountsIdsList.add(accountId)
        }

        selectedStateForItemLD.value = !isWasSelected to accountId
        selectedItemsQuantityTextLD.value = selectedAccountsIdsList.size.toString()
    }

    fun onSelectedModeFinished() {
        for (accountId in selectedAccountsIdsList) {
            selectedStateForItemLD.value = false to accountId
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
                        selectedModeStateLD.value = false
                    },
                    { throwable ->
                        shortTimeMessageLiveData.value = throwable.message ?: "unknown"
                    })
                .let(this::bindDisposable)
        }
    }
}