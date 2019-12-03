package com.ak.passwordsaver.presentation.screens.passwordmanage.add

import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.data.model.db.entities.PasswordDBEntity
import com.ak.passwordsaver.domain.passwords.IPasswordsInteractor
import com.ak.passwordsaver.presentation.screens.passwordmanage.BaseManagePasswordPresenter
import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@InjectViewState
class AddNewPasswordPresenter : BaseManagePasswordPresenter<IAddNewPasswordView>() {

    @Inject
    lateinit var mPasswordsInteractor: IPasswordsInteractor

    init {
        PSApplication.appInstance.getApplicationComponent().inject(this)
    }

    override fun onManagePasswordAction(name: String, content: String) {
        mPasswordsInteractor.addNewPassword(
            PasswordDBEntity(
                name,
                content,
                mSelectedAvatarPath
            )
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { isSuccess ->
                    if (isSuccess) {
                        viewState.displaySuccessPasswordManageAction()
                    }
                },
                { throwable ->
                    handleError(throwable)
                })
            .let(this::bindDisposable)
    }
}