package com.ak.passwordsaver.presentation.screens.passwordmanage.edit

import com.ak.domain.data.model.db.entities.PasswordDBEntity
import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.domain.passwords.IPasswordsInteractor
import com.ak.passwordsaver.presentation.screens.passwordmanage.BaseManagePasswordPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class EditPasswordPresenter @Inject constructor(
    private val passwordsInteractor: IPasswordsInteractor
) : BaseManagePasswordPresenter<IEditPasswordView>() {

    private var mPasswordEntityForEdit: PasswordDBEntity? = null

    init {
        PSApplication.appInstance.getApplicationComponent().inject(this)
    }

    fun loadPasswordData(passwordId: Long) {
        passwordsInteractor.getPasswordById(passwordId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { passwordEntity ->
                    mPasswordEntityForEdit = passwordEntity
                    viewState.displayPasswordData(
                        passwordEntity.passwordName,
                        passwordEntity.passwordContent
                    )
                },
                { throwable ->
                    viewState.showShortTimeMessage("aaaaaa, blyat")
                }
            )
            .let(this::bindDisposable)
    }

    override fun onManagePasswordAction(name: String, content: String) {
        if (mPasswordEntityForEdit == null) {
            viewState.showShortTimeMessage("chto-to poshlo ne tak")
            return
        }

        val updatedPassword = mPasswordEntityForEdit!!.also {
            it.passwordName = name
            it.passwordContent = content
            it.passwordAvatarPath = mSelectedAvatarPath ?: ""
        }

        passwordsInteractor.updatePassword(updatedPassword)
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