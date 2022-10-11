package com.ak.feature_tabsettings_impl.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ak.base.scopes.FeatureScope
import com.ak.base.viewmodel.IViewModelAssistedFactory
import com.ak.base.viewmodel.annotation.ViewModelInjectKey
import com.ak.feature_tabsettings_impl.about.AboutSettingsViewModel
import com.ak.feature_tabsettings_impl.debug.DebugSettingsViewModel
import com.ak.feature_tabsettings_impl.design.DesignSettingsViewModel
import com.ak.feature_tabsettings_impl.main.SettingsViewModel
import com.ak.feature_tabsettings_impl.privacy.PrivacySettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Named

@Module
abstract class TabSettingsViewModelsModule {

    companion object {
        const val SETTINGS_VIEW_MODELS_FACTORY_KEY = "settings_view_models_factory_key"
    }

    @Binds
    @FeatureScope
    @Named(SETTINGS_VIEW_MODELS_FACTORY_KEY)
    abstract fun bindViewModelFactory(factory: SettingsViewModelsFactoryComposite): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @FeatureScope
    @ViewModelInjectKey(SettingsViewModel::class)
    abstract fun bindSettingsViewModelAssistedFactory(
        factory: SettingsViewModelAssistedFactory
    ): IViewModelAssistedFactory<out ViewModel>

    @Binds
    @IntoMap
    @FeatureScope
    @ViewModelInjectKey(AboutSettingsViewModel::class)
    abstract fun bindAboutSettingsViewModelAssistedFactory(
        factory: AboutSettingsViewModelAssistedFactory
    ): IViewModelAssistedFactory<out ViewModel>

    @Binds
    @IntoMap
    @FeatureScope
    @ViewModelInjectKey(DesignSettingsViewModel::class)
    abstract fun bindDesignSettingsViewModelAssistedFactory(
        factory: DesignSettingsViewModelAssistedFactory
    ): IViewModelAssistedFactory<out ViewModel>

    @Binds
    @IntoMap
    @FeatureScope
    @ViewModelInjectKey(PrivacySettingsViewModel::class)
    abstract fun bindPrivacySettingsViewModelAssistedFactory(
        factory: PrivacySettingsViewModelAssistedFactory
    ): IViewModelAssistedFactory<out ViewModel>

    @Binds
    @IntoMap
    @FeatureScope
    @ViewModelInjectKey(DebugSettingsViewModel::class)
    abstract fun bindDebugSettingsViewModelAssistedFactory(
        factory: DebugSettingsViewModelAssistedFactory
    ): IViewModelAssistedFactory<out ViewModel>
}