package com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage

import android.graphics.Bitmap
import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ak.base.constants.AppConstants
import com.ak.base.livedata.SingleEventLiveData
import com.ak.base.utils.PSUtils
import com.ak.base.viewmodel.BasePSViewModel
import com.ak.core_repo_api.intefaces.IPSInternalStorageManager
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.feature_tabpasswords_impl.R
import com.ak.feature_tabpasswords_impl.domain.usecase.PasswordDataCheckException
import com.ak.feature_tabpasswords_impl.screens.logic.IBitmapDecoderManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

abstract class BaseManagePasswordViewModel(
    protected val bitmapDecoderManager: IBitmapDecoderManager,
    protected val internalStorageManager: IPSInternalStorageManager,
    protected val resourceManager: IResourceManager,
): BasePSViewModel() {

    private var nameChangeDis: Disposable? = null
    private var isAvatarDisplayed = false

    private val passwordNameChangeSubject = BehaviorSubject.create<String>()
    protected var selectedAvatarPath: String? = null

    protected val passwordAvatarChooserImageLiveData = MutableLiveData<Bitmap?>()
    protected val passwordAvatarTextLiveData = MutableLiveData<String>()
    protected val passwordNameInputErrorLiveData = MutableLiveData<String?>()
    protected val passwordContentInputErrorLiveData = MutableLiveData<String?>()
    protected val successManageLiveData = SingleEventLiveData<Unit?>()

    fun subscribeToAvatarChooserImage(): LiveData<Bitmap?> = passwordAvatarChooserImageLiveData
    fun subscribeToAvatarText(): LiveData<String> = passwordAvatarTextLiveData
    fun subscribeToNameInputError(): LiveData<String?> = passwordNameInputErrorLiveData
    fun subscribeToContentInputError(): LiveData<String?> = passwordContentInputErrorLiveData
    fun subscribeToSuccessPasswordManage(): LiveData<Unit?> = successManageLiveData

    @CallSuper
    open fun onManagePasswordAction(name: String, content: String) {
        passwordNameInputErrorLiveData.value = null
        passwordContentInputErrorLiveData.value = null
    }

    fun onAvatarDisplayStateChanged(isDisplayed: Boolean) {
        isAvatarDisplayed = isDisplayed
        if (isAvatarDisplayed) {
            nameChangeDis?.dispose()
        } else {
            observePasswordNameChanges()
        }
    }

    fun onPasswordNameTextChanged(currentName: String) {
        passwordNameChangeSubject.onNext(currentName)
    }

    fun onGalleryAvatarSelected(avatarUriPath: String) {
        val bitmapImage = bitmapDecoderManager.decodeBitmap(avatarUriPath)
        bitmapImage?.let {
            val fileImagePath = internalStorageManager.saveBitmapImage(it)
            if (fileImagePath != null) {
                selectedAvatarPath = fileImagePath
                passwordAvatarChooserImageLiveData.value = it
            } else {
                shortTimeMessageLiveData.value = resourceManager.getString(R.string.unknown_error_message)
            }
        }
    }

    fun onCameraImageSelected(fileImagePath: String) {
        val bitmap = internalStorageManager.getBitmapImageFromPath(fileImagePath)
        bitmap?.let {
            selectedAvatarPath = fileImagePath
            passwordAvatarChooserImageLiveData.value = it
        }
    }

    fun onAvatarRemoved() {
        selectedAvatarPath = null
        passwordAvatarChooserImageLiveData.value = null
    }

    private fun observePasswordNameChanges() {
        nameChangeDis = passwordNameChangeSubject.toFlowable(BackpressureStrategy.LATEST)
            .subscribeOn(Schedulers.io())
            .debounce(AppConstants.TEXT_INPUT_DEBOUNCE, TimeUnit.MILLISECONDS)
            .map(PSUtils::getAbbreviationFormName)
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(passwordAvatarTextLiveData::setValue)
        bindDisposable(nameChangeDis!!)
    }

    protected fun handleError(throwable: Throwable) {
        when (throwable) {
            is PasswordDataCheckException -> {
                dataCheckExceptionHandle(throwable)
            }
            else -> {
                shortTimeMessageLiveData.value = throwable.message ?: ""
            }
        }
    }

    private fun dataCheckExceptionHandle(th: PasswordDataCheckException) {
        for (field in th.emptyFields) {
            when (field) {
                PasswordDataCheckException.PASSWORD_NAME_FIELD -> passwordNameInputErrorLiveData.value = (
                    resourceManager.getString(
                        R.string.empty_error_message,
                        resourceManager.getString(R.string.name_field_name)
                    )
                )
                PasswordDataCheckException.PASSWORD_CONTENT_FIELD -> passwordContentInputErrorLiveData.value = (
                    resourceManager.getString(
                        R.string.empty_error_message,
                        resourceManager.getString(R.string.content_field_name)
                    )
                )
            }
        }

        for (field in th.incorrectDataFields) {
            // TODO: incorrect fields handling
        }
    }
}