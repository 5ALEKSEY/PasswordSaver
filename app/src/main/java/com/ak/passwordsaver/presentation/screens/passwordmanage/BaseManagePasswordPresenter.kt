package com.ak.passwordsaver.presentation.screens.passwordmanage

import com.ak.base.managers.bitmapdecoder.IBitmapDecoderManager
import com.ak.domain.data.model.internalstorage.IPSInternalStorageManager
import com.ak.passwordsaver.domain.passwords.usecase.PasswordDataCheckException
import com.ak.passwordsaver.presentation.base.BasePSPresenter
import com.ak.passwordsaver.presentation.base.constants.AppConstants
import com.ak.passwordsaver.utils.PSUtils
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

abstract class BaseManagePasswordPresenter<MV : IBaseManagePasswordView> : BasePSPresenter<MV>() {

    @Inject
    lateinit var bitmapDecoderManager: IBitmapDecoderManager
    @Inject
    lateinit var internalStorageManager: IPSInternalStorageManager

    private var mNameChangeDis: Disposable? = null
    private var mIsAvatarDisplayed = false

    private val mPasswordNameChangeSubject = BehaviorSubject.create<String>()
    protected var mSelectedAvatarPath: String? = null

    init {
        observePasswordNameChanges()
    }

    abstract fun onManagePasswordAction(name: String, content: String)

    fun onAvatarDisplayStateChanged(isDisplayed: Boolean) {
        mIsAvatarDisplayed = isDisplayed
        if (mIsAvatarDisplayed) {
            mNameChangeDis?.dispose()
        } else {
            observePasswordNameChanges()
        }
    }

    fun onPasswordNameTextChanged(currentName: String) {
        mPasswordNameChangeSubject.onNext(currentName)
    }

    fun onGalleryAvatarSelected(avatarUriPath: String) {
        val bitmapImage = bitmapDecoderManager.decodeBitmap(avatarUriPath)
        bitmapImage?.let {
            val fileImagePath = internalStorageManager.saveBitmapImage(it)
            if (fileImagePath != null) {
                mSelectedAvatarPath = fileImagePath
                viewState.displayPasswordAvatarChooserImage(it)
            } else {
                viewState.showShortTimeMessage("aaaaa, blyat'")
            }
        }
    }

    fun onCameraImageSelected(fileImagePath: String) {
        val bitmap = internalStorageManager.getBitmapImageFromPath(fileImagePath)
        bitmap?.let {
            mSelectedAvatarPath = fileImagePath
            viewState.displayPasswordAvatarChooserImage(it)
        }
    }

    fun onAvatarRemoved() {
        mSelectedAvatarPath = null
        viewState.deletePasswordAvatarChooserImage()
    }

    private fun observePasswordNameChanges() {
        mNameChangeDis = mPasswordNameChangeSubject.toFlowable(BackpressureStrategy.LATEST)
            .subscribeOn(Schedulers.io())
            .debounce(AppConstants.TEXT_INPUT_DEBOUNCE, TimeUnit.MILLISECONDS)
            .map(PSUtils::getAbbreviationFormPasswordName)
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(viewState::drawTextForPasswordAvatar)
        bindDisposable(mNameChangeDis!!)
    }

    protected fun handleError(throwable: Throwable) {
        when (throwable) {
            is PasswordDataCheckException -> {
                dataCheckExceptionHandle(throwable)
            }
            else -> {
                viewState.showShortTimeMessage(throwable.message ?: "")
            }
        }
    }

    private fun dataCheckExceptionHandle(th: PasswordDataCheckException) {
        for (field in th.emptyFields) {
            when (field) {
                PasswordDataCheckException.PASSWORD_NAME_FIELD -> viewState.displayPasswordNameInputError(
                    "Name can't be empty"
                )
                PasswordDataCheckException.PASSWORD_CONTENT_FIELD -> viewState.displayPasswordContentInputError(
                    "Content can't be empty"
                )
            }
        }

        for (field in th.incorrectDataFields) {
            // TODO: incorrect fields handling
        }
    }
}