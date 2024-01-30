package com.example.feature_customthememanager_impl.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ak.base.scopes.FeatureScope
import com.ak.base.viewmodel.IViewModelAssistedFactory
import com.ak.base.viewmodel.annotation.ViewModelInjectKey
import com.example.feature_customthememanager_impl.managetheme.ManageCustomThemeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Named

@Module
abstract class CustomThemeManagerViewModelsModule {

    companion object {
        const val CUSTOM_THEME_VIEW_MODELS_FACTORY_KEY = "custom_theme_view_models_factory_key"
    }

    @Binds
    @FeatureScope
    @Named(CUSTOM_THEME_VIEW_MODELS_FACTORY_KEY)
    abstract fun bindViewModelFactory(factory: CustomThemeManagerFactoryComposite): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @FeatureScope
    @ViewModelInjectKey(ManageCustomThemeViewModel::class)
    abstract fun bindManageCustomThemeViewModelAssistedFactory(
        factory: ManageCustomThemeViewModelAssistedFactory,
    ): IViewModelAssistedFactory<out ViewModel>
}