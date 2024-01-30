package com.ak.feature_tabsettings_impl.base

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.ak.base.ui.BasePSFragment
import com.ak.base.viewmodel.BasePSViewModel
import com.ak.feature_tabsettings_impl.di.FeatureTabSettingsComponent
import com.ak.feature_tabsettings_impl.di.FeatureTabSettingsComponentInitializer
import com.ak.feature_tabsettings_impl.di.TabSettingsViewModelsModule
import javax.inject.Inject
import javax.inject.Named

abstract class BaseSettingsModuleFragment<VM : BasePSViewModel> : BasePSFragment<VM>() {

    @Inject
    @field:Named(TabSettingsViewModelsModule.SETTINGS_VIEW_MODELS_FACTORY_KEY)
    protected lateinit var viewModelsFactory: ViewModelProvider.Factory

    override fun injectFragment(appContext: Context) {
        if (appContext is FeatureTabSettingsComponentInitializer) {
            injectFragment(appContext.initializeTabSettingsComponent())
        }
    }

    abstract fun injectFragment(component: FeatureTabSettingsComponent)
}