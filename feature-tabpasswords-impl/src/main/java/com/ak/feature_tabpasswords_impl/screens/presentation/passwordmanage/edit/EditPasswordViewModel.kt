package com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ak.core_repo_api.intefaces.IPSInternalStorageManager
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.feature_tabpasswords_api.interfaces.IPasswordsInteractor
import com.ak.feature_tabpasswords_api.interfaces.PasswordFeatureEntity
import com.ak.feature_tabpasswords_impl.R
import com.ak.feature_tabpasswords_impl.domain.entity.PasswordDomainEntity
import com.ak.feature_tabpasswords_impl.domain.entity.mapToDomainEntity
import com.ak.feature_tabpasswords_impl.screens.logic.IBitmapDecoderManager
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.BaseManagePasswordViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class EditPasswordViewModel @Inject constructor(
    private val passwordsInteractor: IPasswordsInteractor,
    bitmapDecoderManager: IBitmapDecoderManager,
    internalStorageManager: IPSInternalStorageManager,
    resourceManager: IResourceManager,
) : BaseManagePasswordViewModel(
    bitmapDecoderManager,
    internalStorageManager,
    resourceManager,
) {

    private var passwordEntityForEdit: PasswordDomainEntity? = null

    private val passwordDataLiveData = MutableLiveData<Pair<String, String>>()

    fun subscribeToPasswordData(): LiveData<Pair<String, String>> = passwordDataLiveData

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
                        passwordAvatarChooserImageLiveData.value = internalStorageManager.getBitmapImageFromPath(avatarPath)
                    }
                    passwordDataLiveData.value = passwordEntity.getPasswordName() to passwordEntity.getPasswordContent()
                },
                { throwable ->
                    shortTimeMessageLiveData.value = resourceManager.getString(R.string.unknown_error_message)
                }
            )
            .let(this::bindDisposable)
    }

    override fun onManagePasswordAction(name: String, content: String) {
        super.onManagePasswordAction(name, content)
        if (passwordEntityForEdit == null) {
            shortTimeMessageLiveData.value = resourceManager.getString(R.string.unknown_error_message)
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
                        successManageLiveData.call()
                    }
                },
                { throwable ->
                    handleError(throwable)
                })
            .let(this::bindDisposable)
    }
}