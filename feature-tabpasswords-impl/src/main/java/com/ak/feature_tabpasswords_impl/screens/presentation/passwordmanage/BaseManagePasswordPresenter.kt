package com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage

import com.ak.base.constants.AppConstants
import com.ak.base.presenter.BasePSPresenter
import com.ak.base.utils.PSUtils
import com.ak.core_repo_api.intefaces.IPSInternalStorageManager
import com.ak.feature_tabpasswords_impl.domain.usecase.PasswordDataCheckException
import com.ak.feature_tabpasswords_impl.screens.logic.IBitmapDecoderManager
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

abstract class BaseManagePasswordPresenter<MV : IBaseManagePasswordView> : BasePSPresenter<MV>() {

    @Inject
    protected lateinit var bitmapDecoderManager: IBitmapDecoderManager
    @Inject
    protected lateinit var internalStorageManager: IPSInternalStorageManager

    private var nameChangeDis: Disposable? = null
    private var isAvatarDisplayed = false

    private val passwordNameChangeSubject = BehaviorSubject.create<String>()
    protected var selectedAvatarPath: String? = null

    init {
        observePasswordNameChanges()
    }

    abstract fun onManagePasswordAction(name: String, content: String)

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
                viewState.displayPasswordAvatarChooserImage(it)
            } else {
                viewState.showShortTimeMessage("aaaaa, blyat'")
            }
        }
    }

    fun onCameraImageSelected(fileImagePath: String) {
        val bitmap = internalStorageManager.getBitmapImageFromPath(fileImagePath)
        bitmap?.let {
            selectedAvatarPath = fileImagePath
            viewState.displayPasswordAvatarChooserImage(it)
        }
    }

    fun onAvatarRemoved() {
        selectedAvatarPath = null
        viewState.deletePasswordAvatarChooserImage()
    }

    private fun observePasswordNameChanges() {
        nameChangeDis = passwordNameChangeSubject.toFlowable(BackpressureStrategy.LATEST)
            .subscribeOn(Schedulers.io())
            .debounce(AppConstants.TEXT_INPUT_DEBOUNCE, TimeUnit.MILLISECONDS)
            .map(PSUtils::getAbbreviationFormName)
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(viewState::drawTextForPasswordAvatar)
        bindDisposable(nameChangeDis!!)
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