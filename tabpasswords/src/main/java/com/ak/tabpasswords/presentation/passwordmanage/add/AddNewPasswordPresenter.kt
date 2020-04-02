package com.ak.tabpasswords.presentation.passwordmanage.add

import com.ak.domain.data.model.db.entities.PasswordDBEntity
import com.ak.domain.passwords.IPasswordsInteractor
import com.ak.tabpasswords.di.PasswordsComponentProvider
import com.ak.tabpasswords.presentation.passwordmanage.BaseManagePasswordPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class AddNewPasswordPresenter @Inject constructor(
    private val passwordsInteractor: IPasswordsInteractor
) : BaseManagePasswordPresenter<IAddNewPasswordView>() {

    init {
        (applicationContext as PasswordsComponentProvider)
            .providePasswordsComponent()
            .inject(this)
    }

    override fun onManagePasswordAction(name: String, content: String) {
        passwordsInteractor.addNewPassword(
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