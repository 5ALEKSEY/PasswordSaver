package com.ak.feature_tabpasswords_impl.screens.presentation.passwords

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ak.base.viewmodel.BasePSViewModel
import com.ak.feature_tabpasswords_api.interfaces.IPasswordsInteractor
import javax.inject.Inject
import kotlinx.coroutines.launch

class PasswordsActionModeViewModel @Inject constructor(
    private val passwordsInteractor: IPasswordsInteractor
): BasePSViewModel() {

    private var selectedPasswordsIdsList = arrayListOf<Long>()
    private var isSelectedModeActive = false

    private val selectedModeStateLD = MutableLiveData<Boolean>()
    private val selectedStateForItemLD = MutableLiveData<Pair<Boolean, Long>>()
    private val selectedItemsQuantityTextLD = MutableLiveData<String>()

    fun subscribeToSelectedModeState(): LiveData<Boolean> = selectedModeStateLD
    fun subscribeToSelectedStateForItem(): LiveData<Pair<Boolean, Long>> = selectedStateForItemLD
    fun subscribeToSelectedItemsQuantityText(): LiveData<String> = selectedItemsQuantityTextLD

    fun onPasswordItemSelect(passwordId: Long) {
        if (!isSelectedModeActive) {
            selectedModeStateLD.value = true
            isSelectedModeActive = true
        }

        handleNewItemSelection(passwordId)
    }

    private fun handleNewItemSelection(passwordId: Long) {
        val isWasSelected = selectedPasswordsIdsList.contains(passwordId)

        if (isWasSelected) {
            selectedPasswordsIdsList.remove(passwordId)
            if (selectedPasswordsIdsList.isEmpty()) {
                selectedStateForItemLD.value = !isWasSelected to passwordId
                selectedModeStateLD.value = false
                return
            }
        } else {
            selectedPasswordsIdsList.add(passwordId)
        }

        selectedStateForItemLD.value = !isWasSelected to passwordId
        selectedItemsQuantityTextLD.value = getActionModeTitleText()
    }

    fun onSelectedModeFinished() {
        if (!isSelectedModeActive) return

        for (passwordId in selectedPasswordsIdsList) {
            selectedStateForItemLD.value = false to passwordId
        }
        isSelectedModeActive = false
        selectedModeStateLD.value = false
        selectedPasswordsIdsList.clear()
    }

    fun onDeleteSelectedInActionMode() {
        if (selectedPasswordsIdsList.isNotEmpty()) {
            viewModelScope.launch {
                passwordsInteractor.deletePasswordsByIds(selectedPasswordsIdsList)
                selectedModeStateLD.value = false
            }
        }
    }

    private fun getActionModeTitleText() = selectedPasswordsIdsList.size.toString()
}