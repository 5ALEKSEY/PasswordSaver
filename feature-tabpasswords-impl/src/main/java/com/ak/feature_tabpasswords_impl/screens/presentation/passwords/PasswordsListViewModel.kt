package com.ak.feature_tabpasswords_impl.screens.presentation.passwords

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ak.base.constants.AppConstants
import com.ak.base.livedata.SingleEventLiveData
import com.ak.base.viewmodel.BasePSViewModel
import com.ak.core_repo_api.intefaces.IDateAndTimeManager
import com.ak.core_repo_api.intefaces.IPSInternalStorageManager
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.core_repo_api.intefaces.ISettingsPreferencesManager
import com.ak.feature_tabpasswords_api.interfaces.IPasswordsInteractor
import com.ak.feature_tabpasswords_api.interfaces.PasswordFeatureEntity
import com.ak.feature_tabpasswords_impl.R
import com.ak.feature_tabpasswords_impl.screens.adapter.PasswordItemModel
import com.ak.feature_tabpasswords_impl.screens.logic.IDataBufferManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class PasswordsListViewModel @Inject constructor(
    private val passwordsInteractor: IPasswordsInteractor,
    private val internalStorageManager: IPSInternalStorageManager,
    private val dataBufferManager: IDataBufferManager,
    private val resourceManager: IResourceManager,
    private val dateAndTimeManager: IDateAndTimeManager,
) : BasePSViewModel() {

    private val loadingStateLD = MutableLiveData<Boolean>()
    private val emptyPasswordsStateLD = MutableLiveData<Boolean>()
    private val showEditPasswordScreenLD = SingleEventLiveData<Long>()
    private val toolbarScrollingStateLD = MutableLiveData<Boolean>()
    private val passwordsListLD = MutableLiveData<List<PasswordItemModel>>()

    fun subscribeToLoadingState(): LiveData<Boolean> = loadingStateLD
    fun subscribeEmptyPasswordState(): LiveData<Boolean> = emptyPasswordsStateLD
    fun subscribeToShowEditPasswordScreen(): LiveData<Long> = showEditPasswordScreenLD
    fun subscribeToToolbarScrollingState(): LiveData<Boolean> = toolbarScrollingStateLD
    fun subscribeToPasswordsList(): LiveData<List<PasswordItemModel>> = passwordsListLD

    fun loadPasswords() {
        passwordsInteractor.getAllPasswords()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                loadingStateLD.value = true
                emptyPasswordsStateLD.value = false
            }
            .subscribe(
                { list ->
                    val listForDisplay = convertFeatureEntitiesList(list)
                    loadingStateLD.value = false
                    passwordsListLD.value = listForDisplay
                    emptyPasswordsStateLD.value = list.isEmpty()
                    handleListForDisplay(listForDisplay)
                },
                { throwable ->
                    Log.d("de", "dede")
                })
            .let(this::bindDisposable)
    }

    fun onCopyPasswordAction(passwordId: Long) {
        getPasswordDataAndStartAction(passwordId) {
            dataBufferManager.copyStringData(it.getPasswordContent())
            shortTimeMessageLiveData.value = resourceManager.getString(R.string.copied_to_clipboard_message)
        }
    }

    fun onEditPasswordAction(passwordId: Long) {
        showEditPasswordScreenLD.value = passwordId
    }

    fun onDeletePasswordAction(passwordId: Long) {
        passwordsInteractor.deletePasswordById(passwordId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                },
                { throwable ->
                    shortTimeMessageLiveData.value = throwable.message ?: "unknown"
                })
            .let(this::bindDisposable)
    }

    fun pinPassword(passwordId: Long) {
        passwordsInteractor.pinPassword(passwordId, dateAndTimeManager.getCurrentTimeInMillis())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                },
                { throwable ->
                    shortTimeMessageLiveData.value = throwable.message ?: "unknown"
                })
            .let(this::bindDisposable)
    }

    fun unpinPassword(passwordId: Long) {
        passwordsInteractor.unpinPassword(passwordId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                },
                { throwable ->
                    shortTimeMessageLiveData.value = throwable.message ?: "unknown"
                })
            .let(this::bindDisposable)
    }

    private inline fun getPasswordDataAndStartAction(passwordsId: Long, crossinline action: (entity: PasswordFeatureEntity) -> Unit) {
        passwordsInteractor.getPasswordById(passwordsId)
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