package com.ak.feature_security_impl.di.modules

import androidx.lifecycle.ViewModel
import com.ak.base.viewmodel.BaseViewModelFactory
import com.ak.base.viewmodel.IViewModelAssistedFactory
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.core_repo_api.intefaces.ISettingsPreferencesManager
import com.ak.feature_security_api.interfaces.IPSBiometricManager
import com.ak.feature_security_impl.auth.SecurityViewModel
import javax.inject.Inject

class SecurityViewModelsFactoryComposite @Inject constructor(
    viewModels: MutableMap<Class<out ViewModel>, IViewModelAssistedFactory<out ViewModel>>
) : BaseViewModelFactory(viewModels)

class SecurityViewModelAssistedFactory @Inject constructor(
    private val settingsPreferencesManager: ISettingsPreferencesManager,
    private val psBiometricManager: IPSBiometricManager,
    private val resourceManager: IResourceManager,
): IViewModelAssistedFactory<SecurityViewModel> {
    override fun create(): SecurityViewModel {
        return SecurityViewModel(
            settingsPreferencesManager = settingsPreferencesManager,
            psBiometricManager = psBiometricManager,
            resourceManager = resourceManager,
        )
    }
}