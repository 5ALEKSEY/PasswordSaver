package com.ak.feature_tabsettings_impl.di

import android.content.Context
import com.ak.base.scopes.FeatureScope
import com.ak.core_repo_api.api.CoreRepositoryApi
import com.ak.feature_appupdate_api.api.FeatureAppUpdateApi
import com.ak.feature_security_api.api.FeatureSecurityApi
import com.ak.feature_tabsettings_api.FeatureTabSettingsApi
import com.ak.feature_tabsettings_impl.about.AboutSettingsFragment
import com.ak.feature_tabsettings_impl.debug.DebugSettingsFragment
import com.ak.feature_tabsettings_impl.design.DesignSettingsFragment
import com.ak.feature_tabsettings_impl.main.SettingsFragment
import com.ak.feature_tabsettings_impl.privacy.PrivacySettingsFragment
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [
        TabSettingsViewModelsModule::class,
        CustomThemesModule::class,
    ],
    dependencies = [FeatureTabSettingsDependencies::class]
)
@FeatureScope
abstract class FeatureTabSettingsComponent : FeatureTabSettingsApi {

    companion object {
        @Volatile
        private var featureTabSettingsComponent: FeatureTabSettingsComponent? = null

        fun initializeAndGet(dependencies: FeatureTabSettingsDependencies, applicationContext: Context): FeatureTabSettingsComponent {
            if (featureTabSettingsComponent == null) {
                featureTabSettingsComponent = DaggerFeatureTabSettingsComponent.builder()
                    .injectAppContext(applicationContext)
                    .provideDependencies(dependencies)
                    .build()
            }

            return requireNotNull(featureTabSettingsComponent) {
                "FeatureTabSettingsComponent is null. initializeAndGet() didn't initialize it"
            }
        }

        fun clear() {
            featureTabSettingsComponent = null
        }
    }

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun injectAppContext(context: Context): Builder
        fun provideDependencies(dependencies: FeatureTabSettingsDependencies): Builder
        fun build(): FeatureTabSettingsComponent
    }

    abstract fun inject(fragment: SettingsFragment)
    abstract fun inject(fragment: PrivacySettingsFragment)
    abstract fun inject(fragment: DesignSettingsFragment)
    abstract fun inject(fragment: AboutSettingsFragment)
    abstract fun inject(fragment: DebugSettingsFragment)

    @Component(dependencies = [CoreRepositoryApi::class, FeatureSecurityApi::class, FeatureAppUpdateApi::class])
    @FeatureScope
    interface FeatureTabSettingsDependenciesComponent : FeatureTabSettingsDependencies
}