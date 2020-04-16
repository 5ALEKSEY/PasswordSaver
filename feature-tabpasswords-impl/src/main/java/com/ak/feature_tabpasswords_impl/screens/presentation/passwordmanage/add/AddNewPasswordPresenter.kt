package com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.add

import com.ak.feature_tabpasswords_api.interfaces.IPasswordsInteractor
import com.ak.feature_tabpasswords_impl.di.FeatureTabPasswordsComponent
import com.ak.feature_tabpasswords_impl.domain.entity.PasswordDomainEntity
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.BaseManagePasswordPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class AddNewPasswordPresenter @Inject constructor(
    private val passwordsInteractor: IPasswordsInteractor
) : BaseManagePasswordPresenter<IAddNewPasswordView>() {

    init {
        FeatureTabPasswordsComponent.get().inject(this)
    }

    override fun onManagePasswordAction(name: String, content: String) {
        passwordsInteractor.addNewPassword(
            PasswordDomainEntity(
                name,
                selectedAvatarPath,
                content
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