package com.ak.passwordsaver.presentation.screens.passwordmanage.edit

import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.data.model.db.entities.PasswordDBEntity
import com.ak.passwordsaver.domain.passwords.IPasswordsInteractor
import com.ak.passwordsaver.presentation.screens.passwordmanage.BaseManagePasswordPresenter
import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@InjectViewState
class EditPasswordPresenter : BaseManagePasswordPresenter<IEditPasswordView>() {

    @Inject
    lateinit var mPasswordsInteractor: IPasswordsInteractor

    private var mPasswordEntityForEdit: PasswordDBEntity? = null

    init {
        PSApplication.appInstance.getApplicationComponent().inject(this)
    }

    fun loadPasswordData(passwordId: Long) {
        mPasswordsInteractor.getPasswordById(passwordId)
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

        mPasswordsInteractor.updatePassword(updatedPassword)
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