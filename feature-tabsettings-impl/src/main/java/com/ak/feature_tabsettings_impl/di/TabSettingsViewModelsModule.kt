package com.ak.feature_tabsettings_impl.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ak.base.scopes.FeatureScope
import com.ak.base.viewmodel.BaseViewModelFactory
import com.ak.base.viewmodel.annotation.ViewModelInjectKey
import com.ak.feature_tabsettings_impl.about.AboutSettingsViewModel
import com.ak.feature_tabsettings_impl.design.DesignSettingsViewModel
import com.ak.feature_tabsettings_impl.main.SettingsViewModel
import com.ak.feature_tabsettings_impl.privacy.PrivacySettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider

@Module
abstract class TabSettingsViewModelsModule {

    companion object {
        const val SETTINGS_VIEW_MODELS_FACTORY_KEY = "settings_view_models_factory_key"
    }

    @Binds
    @FeatureScope
    @Named(SETTINGS_VIEW_MODELS_FACTORY_KEY)
    abstract fun bindViewModelFactory(factory: SettingsViewModelsFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @FeatureScope
    @ViewModelInjectKey(SettingsViewModel::class)
    abstract fun injectSettingsViewModel(viewModel: SettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @FeatureScope
    @ViewModelInjectKey(AboutSettingsViewModel::class)
    abstract fun injectAboutSettingsViewModel(viewModel: AboutSettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @FeatureScope
    @ViewModelInjectKey(DesignSettingsViewModel::class)
    abstract fun injectDesignSettingsViewModel(viewModel: DesignSettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @FeatureScope
    @ViewModelInjectKey(PrivacySettingsViewModel::class)
    abstract fun injectPrivacySettingsViewModel(viewModel: PrivacySettingsViewModel): ViewModel
}

class SettingsViewModelsFactory @Inject constructor(
    viewModels: MutableMap<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : BaseViewModelFactory(viewModels)