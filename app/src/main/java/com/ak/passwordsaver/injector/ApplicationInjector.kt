package com.ak.passwordsaver.injector

import com.ak.core_repo_api.api.CoreRepositoryApi
import com.ak.core_repo_impl.di.CoreRepositoryComponent
import com.ak.feature_appupdate_api.api.FeatureAppUpdateApi
import com.ak.feature_appupdate_impl.di.FeatureAppUpdateComponent
import com.ak.feature_encryption_api.api.FeatureEncryptionApi
import com.ak.feature_encryption_impl.di.FeatureEncryptionComponent
import com.ak.feature_security_api.api.FeatureSecurityApi
import com.ak.feature_security_impl.di.DaggerFeatureSecurityComponent_FeatureSecurityDependenciesComponent
import com.ak.feature_security_impl.di.FeatureSecurityComponent
import com.ak.feature_tabaccounts_impl.di.DaggerFeatureTabAccountsComponent_FeatureTabAccountsDependenciesComponent
import com.ak.feature_tabaccounts_impl.di.FeatureTabAccountsComponent
import com.ak.feature_tabaccounts_impl.di.FeatureTabAccountsDependencies
import com.ak.feature_tabpasswords_impl.di.DaggerFeatureTabPasswordsComponent_FeatureTabPasswordsDependenciesComponent
import com.ak.feature_tabpasswords_impl.di.FeatureTabPasswordsComponent
import com.ak.feature_tabpasswords_impl.di.FeatureTabPasswordsDependencies
import com.ak.feature_tabsettings_impl.di.DaggerFeatureTabSettingsComponent_FeatureTabSettingsDependenciesComponent
import com.ak.feature_tabsettings_impl.di.FeatureTabSettingsComponent
import com.ak.feature_tabsettings_impl.di.FeatureTabSettingsDependencies
import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.R
import com.ak.passwordsaver.di.AppComponent
import com.ak.passwordsaver.di.DaggerAppComponent_AppComponentDependenciesComponent

object ApplicationInjector {

    fun onDestinationIdChanged(destinationId: Int) {
        when (destinationId) {
            R.id.passwordsListFragment -> {
                initTabPasswordsFeature()

                // clear another features
                clearTabSettingsFeature()
                clearTabAccountsFeature()
            }
            R.id.accountsListFragment -> {
                initTabAccountsFeature()

                // clear another features
                clearTabSettingsFeature()
                clearTabPasswordsFeature()
            }
            R.id.settingsFragment -> {
                initTabSettingsFeature()

                // clear another features
                clearTabPasswordsFeature()
                clearTabAccountsFeature()
            }
        }
    }

    fun initAppComponent(): AppComponent {
        return AppComponent.initialize(
                DaggerAppComponent_AppComponentDependenciesComponent.builder()
                    .featureSecurityApi(initSecurityFeature())
                    .featureAppUpdateApi(initFeatureAppUpdate())
                    .coreRepositoryApi(initCoreRepo())
                    .build()
        )
    }

    private fun initTabPasswordsFeature() {
        if (!FeatureTabPasswordsComponent.isInitialized()) {
            FeatureTabPasswordsComponent.initialize(
                    initTabPasswordsDependencies(),
                    PSApplication.appContext
            )
        }
    }

    private fun clearTabPasswordsFeature() {
        if (FeatureTabPasswordsComponent.isInitialized()) {
            FeatureTabPasswordsComponent.get().clearComponent()
        }
    }

    private fun initTabAccountsFeature() {
        if (!FeatureTabAccountsComponent.isInitialized()) {
            FeatureTabAccountsComponent.initialize(
                    initTabAccountsDependencies(),
                    PSApplication.appContext
            )
        }
    }

    private fun clearTabAccountsFeature() {
        if (FeatureTabAccountsComponent.isInitialized()) {
            FeatureTabAccountsComponent.get().clearComponent()
        }
    }

    private fun initTabSettingsFeature() {
        if (!FeatureTabSettingsComponent.isInitialized()) {
            FeatureTabSettingsComponent.initialize(
                    initTabSettingsDependencies(),
                    PSApplication.appContext
            )
        }
    }

    private fun clearTabSettingsFeature() {
        if (FeatureTabSettingsComponent.isInitialized()) {
            FeatureTabSettingsComponent.get().clearComponent()
        }
    }

    private fun initTabPasswordsDependencies(): FeatureTabPasswordsDependencies {
        return DaggerFeatureTabPasswordsComponent_FeatureTabPasswordsDependenciesComponent.builder()
            .coreRepositoryApi(initCoreRepo())
            .featureEncryptionApi(initFeatureEncryption())
            .build()
    }

    private fun initTabAccountsDependencies(): FeatureTabAccountsDependencies {
        return DaggerFeatureTabAccountsComponent_FeatureTabAccountsDependenciesComponent.builder()
            .coreRepositoryApi(initCoreRepo())
            .featureEncryptionApi(initFeatureEncryption())
            .build()
    }

    private fun initTabSettingsDependencies(): FeatureTabSettingsDependencies {
        return DaggerFeatureTabSettingsComponent_FeatureTabSettingsDependenciesComponent.builder()
            .coreRepositoryApi(initCoreRepo())
            .featureSecurityApi(initSecurityFeature())
            .build()
    }

    private fun initSecurityFeature(): FeatureSecurityApi {
        return FeatureSecurityComponent.initAndGet(
                DaggerFeatureSecurityComponent_FeatureSecurityDependenciesComponent.builder()
                    .coreRepositoryApi(initCoreRepo())
                    .build(),
                PSApplication.appContext
        )
    }

    private fun initFeatureEncryption(): FeatureEncryptionApi {
        return FeatureEncryptionComponent.get()
    }

    private fun initFeatureAppUpdate(): FeatureAppUpdateApi {
        return FeatureAppUpdateComponent.get(PSApplication.appContext)
    }

    private fun initCoreRepo(): CoreRepositoryApi {
        return CoreRepositoryComponent.get(PSApplication.appContext)
    }
}