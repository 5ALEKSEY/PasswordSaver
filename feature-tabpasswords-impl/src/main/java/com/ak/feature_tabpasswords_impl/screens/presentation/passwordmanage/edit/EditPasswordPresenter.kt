package com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.edit

import com.ak.feature_tabpasswords_api.interfaces.IPasswordsInteractor
import com.ak.feature_tabpasswords_api.interfaces.PasswordFeatureEntity
import com.ak.feature_tabpasswords_impl.R
import com.ak.feature_tabpasswords_impl.di.FeatureTabPasswordsComponent
import com.ak.feature_tabpasswords_impl.domain.entity.PasswordDomainEntity
import com.ak.feature_tabpasswords_impl.domain.entity.mapToDomainEntity
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.BaseManagePasswordPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class EditPasswordPresenter @Inject constructor(
    private val passwordsInteractor: IPasswordsInteractor
) : BaseManagePasswordPresenter<IEditPasswordView>() {

    private var passwordEntityForEdit: PasswordDomainEntity? = null

    init {
        FeatureTabPasswordsComponent.get().inject(this)
    }

    fun loadPasswordData(passwordId: Long) {
        passwordsInteractor.getPasswordById(passwordId)
            .map(PasswordFeatureEntity::mapToDomainEntity)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { passwordEntity ->
                    passwordEntityForEdit = passwordEntity
                    val avatarPath = passwordEntity.passwordAvatarPathValue
                    if (avatarPath.isNotEmpty()) {
                        selectedAvatarPath = avatarPath
                        viewState.displayPasswordAvatarChooserImage(internalStorageManager.getBitmapImageFromPath(avatarPath))
                    }
                    viewState.displayPasswordData(
                        passwordEntity.getPasswordName(),
                        passwordEntity.getPasswordContent()
                    )
                },
                { throwable ->
                    viewState.showShortTimeMessage(resourceManager.getString(R.string.unknown_error_message))
                }
            )
            .let(this::bindDisposable)
    }

    override fun onManagePasswordAction(name: String, content: String) {
        if (passwordEntityForEdit == null) {
            viewState.showShortTimeMessage(resourceManager.getString(R.string.unknown_error_message))
            return
        }

        val updatedPassword = passwordEntityForEdit!!.also {
            it.passwordNameValue = name
            it.passwordContentValue = content
            it.passwordAvatarPathValue = selectedAvatarPath ?: ""
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