package com.ak.passwordsaver.di.modules

import androidx.lifecycle.ViewModel
import com.ak.base.viewmodel.BaseViewModelFactory
import com.ak.base.viewmodel.IViewModelAssistedFactory
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.core_repo_api.intefaces.preference.ISettingsPreferencesManager
import com.ak.feature_appupdate_api.interfaces.IFeaturesUpdateManager
import com.ak.passwordsaver.presentation.screens.home.HomeViewModel
import javax.inject.Inject

class MainViewModelsFactoryComposite @Inject constructor(
    viewModels: MutableMap<Class<out ViewModel>, IViewModelAssistedFactory<out ViewModel>>
) : BaseViewModelFactory(viewModels)

class HomeViewModelAssistedFactory @Inject constructor(
    private val featuresUpdateManager: IFeaturesUpdateManager,
    private val settingsPreferencesManager: ISettingsPreferencesManager,
    private val resourceManager: IResourceManager,
): IViewModelAssistedFactory<HomeViewModel> {
    override fun create(): HomeViewModel {
        return HomeViewModel(
            featuresUpdateManager = featuresUpdateManager,
            settingsPreferencesManager = settingsPreferencesManager,
            resourceManager = resourceManager,
        )
    }
}