package com.ak.passwordsaver.presentation.screens.addnew

import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.model.db.entities.PasswordDBEntity
import com.ak.passwordsaver.presentation.base.BasePSPresenter
import com.ak.passwordsaver.presentation.screens.addnew.logic.AddNewPasswordInteractor
import com.ak.passwordsaver.presentation.screens.addnew.logic.usecases.PasswordDataCheckException
import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@InjectViewState
class AddNewPasswordPresenter : BasePSPresenter<IAddNewPasswordView>() {

    @Inject
    lateinit var mAddNewPasswordInteractor: AddNewPasswordInteractor

    init {
        PSApplication.appInstance.getApplicationComponent().inject(this)
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