package com.example.feature_customthememanager_impl.base

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.ak.base.ui.BasePSFragment
import com.ak.base.viewmodel.BasePSViewModel
import com.example.feature_customthememanager_impl.di.CustomThemeManagerViewModelsModule
import com.example.feature_customthememanager_impl.di.FeatureCustomThemeManagerComponent
import com.example.feature_customthememanager_impl.di.FeatureCustomThemeManagerComponentInitializer
import javax.inject.Inject
import javax.inject.Named

abstract class BaseCustomThemeModuleFragment<VM : BasePSViewModel> : BasePSFragment<VM>() {

    @Inject
    @field:Named(CustomThemeManagerViewModelsModule.CUSTOM_THEME_VIEW_MODELS_FACTORY_KEY)
    protected lateinit var viewModelsFactory: ViewModelProvider.Factory

    override fun injectFragment(appContext: Context) {
        if (appContext is FeatureCustomThemeManagerComponentInitializer) {
            injectFragment(appContext.initializeFeatureCustomThemeManagerComponent())
        }
    }

    abstract fun injectFragment(component: FeatureCustomThemeManagerComponent)
}