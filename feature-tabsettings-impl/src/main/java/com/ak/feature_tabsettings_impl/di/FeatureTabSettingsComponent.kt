package com.ak.feature_tabsettings_impl.di

import android.content.Context
import com.ak.base.scopes.FeatureScope
import com.ak.core_repo_api.api.CoreRepositoryApi
import com.ak.feature_security_api.api.FeatureSecurityApi
import com.ak.feature_tabsettings_api.FeatureTabSettingsApi
import com.ak.feature_tabsettings_impl.about.AboutSettingsFragment
import com.ak.feature_tabsettings_impl.about.AboutSettingsPresenter
import com.ak.feature_tabsettings_impl.design.DesignSettingsFragment
import com.ak.feature_tabsettings_impl.design.DesignSettingsPresenter
import com.ak.feature_tabsettings_impl.di.modules.AppModule
import com.ak.feature_tabsettings_impl.main.SettingsFragment
import com.ak.feature_tabsettings_impl.main.SettingsPresenter
import com.ak.feature_tabsettings_impl.privacy.PrivacySettingsFragment
import com.ak.feature_tabsettings_impl.privacy.PrivacySettingsPresenter
import dagger.Component

@Component(
        modules = [AppModule::class],
        dependencies = [FeatureTabSettingsDependencies::class]
)
@FeatureScope
abstract class FeatureTabSettingsComponent : FeatureTabSettingsApi {

    companion object {
        @Volatile
        private var featureTabSettingsComponent: FeatureTabSettingsComponent? = null
        private var appContext: Context? = null

        fun initialize(dependencies: FeatureTabSettingsDependencies, applicationContext: Context) {
            if (featureTabSettingsComponent == null) {
                featureTabSettingsComponent = DaggerFeatureTabSettingsComponent.builder()
                    .featureTabSettingsDependencies(dependencies)
                    .build()
                appContext = applicationContext
            }
        }

        fun get(): FeatureTabSettingsComponent = if (featureTabSettingsComponent != null) {
            featureTabSettingsComponent!!
        } else {
            throw IllegalStateException("FeatureTabSettingsComponent is null. initialize() should be called before")
        }

        fun getAppContext(): Context = if (appContext != null) {
            appContext!!
        } else {
            throw IllegalStateException("appContext is null. initialize() should be called before")
        }

        fun isInitialized() = featureTabSettingsComponent != null
    }

    fun clearComponent() {
        featureTabSettingsComponent = null
        appContext = null
    }

    abstract fun inject(fragment: SettingsFragment)
    abstract fun inject(presenter: SettingsPresenter)

    abstract fun inject(fragment: PrivacySettingsFragment)
    abstract fun inject(presenter: PrivacySettingsPresenter)

    abstract fun inject(fragment: DesignSettingsFragment)
    abstract fun inject(presenter: DesignSettingsPresenter)

    abstract fun inject(fragment: AboutSettingsFragment)
    abstract fun inject(presenter: AboutSettingsPresenter)


    @Component(dependencies = [CoreRepositoryApi::class, FeatureSecurityApi::class])
    @FeatureScope
    interface FeatureTabSettingsDependenciesComponent : FeatureTabSettingsDependencies
}