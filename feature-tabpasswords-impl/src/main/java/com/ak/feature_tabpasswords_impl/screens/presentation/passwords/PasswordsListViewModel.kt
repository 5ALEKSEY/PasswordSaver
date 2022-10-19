package com.ak.feature_tabpasswords_impl.screens.presentation.passwords

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ak.base.constants.AppConstants
import com.ak.base.livedata.SingleEventLiveData
import com.ak.base.viewmodel.BasePSViewModel
import com.ak.core_repo_api.intefaces.IDateAndTimeManager
import com.ak.core_repo_api.intefaces.IPSInternalStorageManager
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.feature_tabpasswords_api.interfaces.IPasswordsInteractor
import com.ak.feature_tabpasswords_api.interfaces.PasswordFeatureEntity
import com.ak.feature_tabpasswords_impl.R
import com.ak.feature_tabpasswords_impl.screens.adapter.PasswordItemModel
import com.ak.feature_tabpasswords_impl.screens.logic.IDataBufferManager
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PasswordsListViewModel @Inject constructor(
    private val passwordsInteractor: IPasswordsInteractor,
    private val internalStorageManager: IPSInternalStorageManager,
    private val dataBufferManager: IDataBufferManager,
    private val resourceManager: IResourceManager,
    private val dateAndTimeManager: IDateAndTimeManager,
) : BasePSViewModel() {

    private val primaryLoadingStateLD = MutableLiveData<Boolean>()
    private val secondaryLoadingStateLD = MutableLiveData<Boolean>()
    private val emptyPasswordsStateLD = MutableLiveData<Boolean>()
    private val showEditPasswordScreenLD = SingleEventLiveData<Long>()
    private val toolbarScrollingStateLD = MutableLiveData<Boolean>()
    private val passwordsListLD = MutableLiveData<List<PasswordItemModel>>()

    fun subscribeToPrimaryLoadingState(): LiveData<Boolean> = primaryLoadingStateLD
    fun subscribeToSecondaryLoadingState(): LiveData<Boolean> = secondaryLoadingStateLD
    fun subscribeEmptyPasswordState(): LiveData<Boolean> = emptyPasswordsStateLD
    fun subscribeToShowEditPasswordScreen(): LiveData<Long> = showEditPasswordScreenLD
    fun subscribeToToolbarScrollingState(): LiveData<Boolean> = toolbarScrollingStateLD
    fun subscribeToPasswordsList(): LiveData<List<PasswordItemModel>> = passwordsListLD

    private var loadPasswordsJob: Job? = null

    fun loadPasswords() {
        primaryLoadingStateLD.value = passwordsListLD.value == null
        secondaryLoadingStateLD.value = passwordsListLD.value != null
        emptyPasswordsStateLD.value = false

        loadPasswordsJob?.cancel()
        loadPasswordsJob = viewModelScope.launch(Dispatchers.IO) {
            passwordsInteractor.getAllPasswords().collect {
                val listForDisplay = convertFeatureEntitiesList(it)
                withContext(Dispatchers.Main) {
                    primaryLoadingStateLD.value = false
                    secondaryLoadingStateLD.value = false
                    passwordsListLD.value = listForDisplay
                    emptyPasswordsStateLD.value = listForDisplay.isEmpty()
                    handleListForDisplay(listForDisplay)
                }
            }
        }
    }

    fun onCopyPasswordAction(passwordId: Long) {
        viewModelScope.launch {
            val passwordEntity = passwordsInteractor.getPasswordById(passwordId)
            dataBufferManager.copyStringData(passwordEntity.getPasswordContent())
            shortTimeMessageLiveData.value = resourceManager.getString(R.string.copied_to_clipboard_message)
        }
    }

    fun onEditPasswordAction(passwordId: Long) {
        showEditPasswordScreenLD.value = passwordId
    }

    fun onDeletePasswordAction(passwordId: Long) {
        viewModelScope.launch {
            passwordsInteractor.deletePasswordById(passwordId)
        }
    }

    fun pinPassword(passwordId: Long) {
        viewModelScope.launch {
            passwordsInteractor.pinPassword(passwordId, dateAndTimeManager.getCurrentTimeInMillis())
        }
    }

    fun unpinPassword(passwordId: Long) {
        viewModelScope.launch {
            passwordsInteractor.unpinPassword(passwordId)
        }
    }

    private fun handleListForDisplay(listForDisplay: List<PasswordItemModel>) {
        toolbarScrollingStateLD.value = listForDisplay.size >= AppConstants.TOOLBAR_SCROLL_MIN_PASSWORDS_SIZE
    }

    private fun convertFeatureEntitiesList(entitiesList: List<PasswordFeatureEntity>): List<PasswordItemModel> {
        val resultList = arrayListOf<PasswordItemModel>()
        entitiesList.forEach {
            val avatarBitmap = internalStorageManager.getBitmapImageFromPath(it.getPasswordAvatarPath())
            resultList.add(
                PasswordItemModel(
                    it.getPasswordId()!!,
                    it.getPasswordName(),
                    avatarBitmap,
                    it.getPasswordContent(),
                    it.getPasswordPinTimestamp() != null,
                )
            )
        }
        return resultList
    }
}