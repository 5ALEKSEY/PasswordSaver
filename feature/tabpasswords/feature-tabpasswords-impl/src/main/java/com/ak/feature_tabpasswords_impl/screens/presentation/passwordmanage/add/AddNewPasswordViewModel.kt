package com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.add

import androidx.lifecycle.viewModelScope
import com.ak.core_repo_api.intefaces.IPSInternalStorageManager
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.feature_tabpasswords_api.interfaces.IPasswordsInteractor
import com.ak.feature_tabpasswords_impl.domain.entity.PasswordDomainEntity
import com.ak.feature_tabpasswords_impl.screens.logic.IBitmapDecoderManager
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.BaseManagePasswordViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

class AddNewPasswordViewModel @Inject constructor(
    private val passwordsInteractor: IPasswordsInteractor,
    bitmapDecoderManager: IBitmapDecoderManager,
    internalStorageManager: IPSInternalStorageManager,
    resourceManager: IResourceManager,
) : BaseManagePasswordViewModel(
    bitmapDecoderManager,
    internalStorageManager,
    resourceManager,
) {

    override fun onManagePasswordAction(name: String, content: String) {
        super.onManagePasswordAction(name, content)
        viewModelScope.launch {
            val passwordToAdd = PasswordDomainEntity(
                name = name,
                avatarPath = selectedAvatarPath,
                content = content,
            )
            try {
                passwordsInteractor.addNewPassword(passwordToAdd)
                successManageLiveData.call()
            } catch (error: Throwable) {
                handleError(error)
            }
        }
    }
}