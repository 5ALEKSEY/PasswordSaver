package com.ak.feature_tabsettings_impl.di

import androidx.lifecycle.ViewModel
import com.ak.base.viewmodel.BaseViewModelFactory
import com.ak.base.viewmodel.IViewModelAssistedFactory
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.core_repo_api.intefaces.ISettingsPreferencesManager
import com.ak.feature_appupdate_api.interfaces.IFeaturesUpdateManager
import com.ak.feature_security_api.interfaces.IPSBiometricManager
import com.ak.feature_tabsettings_impl.about.AboutSettingsViewModel
import com.ak.feature_tabsettings_impl.design.DesignSettingsViewModel
import com.ak.feature_tabsettings_impl.main.SettingsViewModel
import com.ak.feature_tabsettings_impl.privacy.PrivacySettingsViewModel
import javax.inject.Inject

class SettingsViewModelsFactoryComposite @Inject constructor(
    viewModels: MutableMap<Class<out ViewModel>, IViewModelAssistedFactory<out ViewModel>>
) : BaseViewModelFactory(viewModels)

class SettingsViewModelAssistedFactory @Inject constructor(
    private val settingsPreferencesManager: ISettingsPreferencesManager,
    private val featuresUpdateManager: IFeaturesUpdateManager,
    private val resourceManager: IResourceManager,
) : IViewModelAssistedFactory<SettingsViewModel> {
    override fun create(): SettingsViewModel {
        return SettingsViewModel(
            settingsPreferencesManager = settingsPreferencesManager,
            featuresUpdateManager = featuresUpdateManager,
            resourceManager = resourceManager,
        )
    }
}

class AboutSettingsViewModelAssistedFactory @Inject constructor() : IViewModelAssistedFactory<AboutSettingsViewModel> {
    override fun create(): AboutSettingsViewModel {
        return AboutSettingsViewModel()
    }
}

class DesignSettingsViewModelAssistedFactory @Inject constructor(
    private val settingsPreferencesManager: ISettingsPreferencesManager,
    private val featuresUpdateManager: IFeaturesUpdateManager,
    private val resourceManager: IResourceManager,
) : IViewModelAssistedFactory<DesignSettingsViewModel> {
    override fun create(): DesignSettingsViewModel {
        return DesignSettingsViewModel(
            settingsPrefManager = settingsPreferencesManager,
            featuresUpdateManager = featuresUpdateManager,
            resourceManager = resourceManager,
        )
    }
}

class PrivacySettingsViewModelAssistedFactory @Inject constructor(
    private val settingsPreferencesManager: ISettingsPreferencesManager,
    private val psBiometricManager: IPSBiometricManager,
    private val featuresUpdateManager: IFeaturesUpdateManager,
    private val resourceManager: IResourceManager,
) : IViewModelAssistedFactory<PrivacySettingsViewModel> {
    override fun create(): PrivacySettingsViewModel {
        return PrivacySettingsViewModel(
            settingsPreferencesManager = settingsPreferencesManager,
            psBiometricManager = psBiometricManager,
            featuresUpdateManager = featuresUpdateManager,
            resourceManager = resourceManager,
        )
    }
}