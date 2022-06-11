package com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.add

import com.ak.feature_tabpasswords_api.interfaces.IPasswordsInteractor
import com.ak.feature_tabpasswords_impl.domain.entity.PasswordDomainEntity
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.BaseManagePasswordViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class AddNewPasswordViewModel @Inject constructor(
    private val passwordsInteractor: IPasswordsInteractor
) : BaseManagePasswordViewModel() {

    override fun onManagePasswordAction(name: String, content: String) {
        super.onManagePasswordAction(name, content)
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
                        successManageLiveData.call()
                    }
                },
                { throwable ->
                    handleError(throwable)
                })
            .let(this::bindDisposable)
    }
}