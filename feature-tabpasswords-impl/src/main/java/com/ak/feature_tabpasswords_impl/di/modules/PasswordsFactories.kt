package com.ak.feature_tabpasswords_impl.di.modules

import androidx.lifecycle.ViewModel
import com.ak.base.viewmodel.BaseViewModelFactory
import com.ak.base.viewmodel.IViewModelAssistedFactory
import com.ak.core_repo_api.intefaces.IDateAndTimeManager
import com.ak.core_repo_api.intefaces.IPSInternalStorageManager
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.core_repo_api.intefaces.ISettingsPreferencesManager
import com.ak.feature_tabpasswords_api.interfaces.IPasswordsInteractor
import com.ak.feature_tabpasswords_impl.screens.logic.IBitmapDecoderManager
import com.ak.feature_tabpasswords_impl.screens.logic.IDataBufferManager
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.add.AddNewPasswordViewModel
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.camera.CameraPickImageViewModel
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.edit.EditPasswordViewModel
import com.ak.feature_tabpasswords_impl.screens.presentation.passwords.PasswordsActionModeViewModel
import com.ak.feature_tabpasswords_impl.screens.presentation.passwords.PasswordsListViewModel
import javax.inject.Inject

class PasswordsViewModelsFactoryComposite @Inject constructor(
    viewModels: MutableMap<Class<out ViewModel>, IViewModelAssistedFactory<out ViewModel>>
) : BaseViewModelFactory(viewModels)

class PasswordsListViewModelAssistedFactory @Inject constructor(
    private val passwordsInteractor: IPasswordsInteractor,
    private val internalStorageManager: IPSInternalStorageManager,
    private val dataBufferManager: IDataBufferManager,
    private val resourceManager: IResourceManager,
    private val dateAndTimeManager: IDateAndTimeManager,
) : IViewModelAssistedFactory<PasswordsListViewModel> {
    override fun create(): PasswordsListViewModel {
        return PasswordsListViewModel(
            passwordsInteractor = passwordsInteractor,
            internalStorageManager = internalStorageManager,
            dataBufferManager = dataBufferManager,
            resourceManager = resourceManager,
            dateAndTimeManager = dateAndTimeManager,
        )
    }
}

class PasswordsActionModeViewModelAssistedFactory @Inject constructor(
    private val passwordsInteractor: IPasswordsInteractor,
) : IViewModelAssistedFactory<PasswordsActionModeViewModel> {
    override fun create(): PasswordsActionModeViewModel {
        return PasswordsActionModeViewModel(passwordsInteractor)
    }
}

class EditPasswordViewModelAssistedFactory @Inject constructor(
    private val passwordsInteractor: IPasswordsInteractor,
    private val bitmapDecoderManager: IBitmapDecoderManager,
    private val internalStorageManager: IPSInternalStorageManager,
    private val resourceManager: IResourceManager,
) : IViewModelAssistedFactory<EditPasswordViewModel> {
    override fun create(): EditPasswordViewModel {
        return EditPasswordViewModel(
            passwordsInteractor = passwordsInteractor,
            bitmapDecoderManager = bitmapDecoderManager,
            internalStorageManager = internalStorageManager,
            resourceManager = resourceManager,
        )
    }
}

class AddNewPasswordViewModelAssistedFactory @Inject constructor(
    private val passwordsInteractor: IPasswordsInteractor,
    private val bitmapDecoderManager: IBitmapDecoderManager,
    private val internalStorageManager: IPSInternalStorageManager,
    private val resourceManager: IResourceManager,
) : IViewModelAssistedFactory<AddNewPasswordViewModel> {
    override fun create(): AddNewPasswordViewModel {
        return AddNewPasswordViewModel(
            passwordsInteractor = passwordsInteractor,
            bitmapDecoderManager = bitmapDecoderManager,
            internalStorageManager = internalStorageManager,
            resourceManager = resourceManager,
        )
    }
}

class CameraPickImageViewModelAssistedFactory @Inject constructor(
    private val internalStorageManager: IPSInternalStorageManager,
    private val resourceManager: IResourceManager,
) : IViewModelAssistedFactory<CameraPickImageViewModel> {
    override fun create(): CameraPickImageViewModel {
        return CameraPickImageViewModel(
            internalStorageManager = internalStorageManager,
            resourceManager = resourceManager,
        )
    }
}