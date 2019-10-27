package com.ak.passwordsaver.presentation.screens.addnew

import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.model.db.entities.PasswordDBEntity
import com.ak.passwordsaver.presentation.base.BasePSPresenter
import com.ak.passwordsaver.presentation.base.constants.AppConstants
import com.ak.passwordsaver.presentation.screens.addnew.logic.AddNewPasswordInteractor
import com.ak.passwordsaver.presentation.screens.addnew.logic.usecases.PasswordDataCheckException
import com.ak.passwordsaver.utils.PSUtils
import com.arellomobile.mvp.InjectViewState
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@InjectViewState
class AddNewPasswordPresenter : BasePSPresenter<IAddNewPasswordView>() {

    private var mNameChangeDis: Disposable? = null
    private var mIsAvatarDisplayed = false

    @Inject
    lateinit var mAddNewPasswordInteractor: AddNewPasswordInteractor

    private val mPasswordNameChangeSubject = BehaviorSubject.create<String>()

    init {
        PSApplication.appInstance.getApplicationComponent().inject(this)
        observePasswordNameChanges()
    }

    fun saveNewPassword(name: String, content: String) {
        mAddNewPasswordInteractor.addNewPassword(PasswordDBEntity(name, content))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { isSuccess ->
                    if (isSuccess) {
                        viewState.displaySuccessPasswordSave()
                    }
                },
                { throwable ->
                    handleError(throwable)
                })
            .let(this::bindDisposable)
    }

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

    private fun handleError(throwable: Throwable) {
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
                PasswordDataCheckException.PASSWORD_NAME_FIELD -> viewState.displayPasswordNameInputError("Name can't be empty")
                PasswordDataCheckException.PASSWORD_CONTENT_FIELD -> viewState.displayPasswordContentInputError("Content can't be empty")
            }
        }

        for (field in th.incorrectDataFields) {
            // TODO: incorrect fields handling
        }
    }
}