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
import com.ak.feature_security_impl.di.FeatureSecurityDependencies
import com.ak.feature_tabaccounts_impl.di.DaggerFeatureTabAccountsComponent_FeatureTabAccountsDependenciesComponent
import com.ak.feature_tabaccounts_impl.di.FeatureTabAccountsComponent
import com.ak.feature_tabaccounts_impl.di.FeatureTabAccountsComponentInitializer
import com.ak.feature_tabaccounts_impl.di.FeatureTabAccountsDependencies
import com.ak.feature_tabpasswords_impl.di.DaggerFeatureTabPasswordsComponent_FeatureTabPasswordsDependenciesComponent
import com.ak.feature_tabpasswords_impl.di.FeatureTabPasswordsComponent
import com.ak.feature_tabpasswords_impl.di.FeatureTabPasswordsComponentInitializer
import com.ak.feature_tabpasswords_impl.di.FeatureTabPasswordsDependencies
import com.ak.feature_tabsettings_impl.di.DaggerFeatureTabSettingsComponent_FeatureTabSettingsDependenciesComponent
import com.ak.feature_tabsettings_impl.di.FeatureTabSettingsComponent
import com.ak.feature_tabsettings_impl.di.FeatureTabSettingsComponentInitializer
import com.ak.feature_tabsettings_impl.di.FeatureTabSettingsDependencies
import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.R
import com.ak.passwordsaver.di.AppComponent
import com.ak.passwordsaver.di.DaggerAppComponent_AppComponentDependenciesComponent
import com.ak.passwordsaver.di.FeatureAppComponentInitializer
import com.example.feature_backup_impl.di.DaggerFeatureBackupComponent_FeatureBackupDependenciesComponent
import com.example.feature_backup_impl.di.FeatureBackupComponent
import com.example.feature_backup_impl.di.FeatureBackupComponentInitializer
import com.example.feature_backup_impl.di.FeatureBackupDependencies
import com.example.feature_customthememanager_impl.di.DaggerFeatureCustomThemeManagerComponent_FeatureCustomThemeManagerDependenciesComponent
import com.example.feature_customthememanager_impl.di.FeatureCustomThemeManagerComponent
import com.example.feature_customthememanager_impl.di.FeatureCustomThemeManagerComponentInitializer
import com.example.feature_customthememanager_impl.di.FeatureCustomThemeManagerDependencies

interface ApplicationComponentsManager :
    ClearComponentsByDestinationChangeManager,
    FeatureAppComponentInitializer,
    FeatureTabPasswordsComponentInitializer,
    FeatureTabAccountsComponentInitializer,
    FeatureTabSettingsComponentInitializer,
    FeatureBackupComponentInitializer,
    FeatureCustomThemeManagerComponentInitializer

interface ClearComponentsByDestinationChangeManager {
    fun onDestinationIdChanged(destinationId: Int)
}

class ApplicationComponentsManagerImpl : ApplicationComponentsManager {

    private val applicationContext by lazy { PSApplication.appContext }

    override fun onDestinationIdChanged(destinationId: Int) {
        when (destinationId) {
            R.id.passwordsListFragment -> {
                // clear another features
                clearTabSettingsFeature()
                clearTabAccountsFeature()
            }
            R.id.accountsListFragment -> {
                // clear another features
                clearTabSettingsFeature()
                clearTabPasswordsFeature()
            }
            R.id.settingsFragment -> {
                // clear another features
                clearTabPasswordsFeature()
                clearTabAccountsFeature()
            }
        }
    }

    override fun initializeAppComponent(): AppComponent {
        return AppComponent.initialize(
            DaggerAppComponent_AppComponentDependenciesComponent.builder()
                .featureSecurityApi(initSecurityComponent())
                .featureAppUpdateApi(initFeatureAppUpdate())
                .coreRepositoryApi(initCoreRepo())
                .build()
        )
    }

    override fun initializeTabPasswordsComponent(): FeatureTabPasswordsComponent {
        return FeatureTabPasswordsComponent.initializeAndGet(initTabPasswordsDependencies(), applicationContext)
    }

    override fun initializeTabAccountsComponent(): FeatureTabAccountsComponent {
        return FeatureTabAccountsComponent.initializeAndGet(initTabAccountsDependencies(), applicationContext)
    }

    override fun initializeTabSettingsComponent(): FeatureTabSettingsComponent {
        return FeatureTabSettingsComponent.initializeAndGet(initTabSettingsDependencies(), applicationContext)
    }

    override fun initializeFeatureBackupComponent(): FeatureBackupComponent {
        return FeatureBackupComponent.initializeAndGet(initFeatureBackupDependencies(), applicationContext)
    }

    override fun initializeFeatureCustomThemeManagerComponent(): FeatureCustomThemeManagerComponent {
        return FeatureCustomThemeManagerComponent.initializeAndGet(initFeatureCustomThemeManagerDependencies(), applicationContext)
    }

    private fun clearTabPasswordsFeature() {
        FeatureTabPasswordsComponent.clear()
    }

    private fun clearTabAccountsFeature() {
        FeatureTabAccountsComponent.clear()
    }

    private fun clearTabSettingsFeature() {
        FeatureTabSettingsComponent.clear()
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
            .featureSecurityApi(initSecurityComponent())
            .featureAppUpdateApi(initFeatureAppUpdate())
            .build()
    }

    private fun initFeatureBackupDependencies(): FeatureBackupDependencies {
        return DaggerFeatureBackupComponent_FeatureBackupDependenciesComponent.builder()
            .coreRepositoryApi(initCoreRepo())
            .featureAppUpdateApi(initFeatureAppUpdate())
            .build()
    }

    private fun initFeatureCustomThemeManagerDependencies(): FeatureCustomThemeManagerDependencies {
        return DaggerFeatureCustomThemeManagerComponent_FeatureCustomThemeManagerDependenciesComponent.builder()
            .coreRepositoryApi(initCoreRepo())
            .build()
    }

    private fun initSecurityComponent(): FeatureSecurityApi {
        return FeatureSecurityComponent.initializeAndGet(initFeatureSecurityDependencies(), applicationContext)
    }

    private fun initFeatureSecurityDependencies(): FeatureSecurityDependencies {
        return DaggerFeatureSecurityComponent_FeatureSecurityDependenciesComponent.builder()
            .coreRepositoryApi(initCoreRepo())
            .build()
    }

    private fun initFeatureEncryption(): FeatureEncryptionApi {
        return FeatureEncryptionComponent.get()
    }

    private fun initFeatureAppUpdate(): FeatureAppUpdateApi {
        return FeatureAppUpdateComponent.get(applicationContext)
    }

    private fun initCoreRepo(): CoreRepositoryApi {
        return CoreRepositoryComponent.get(applicationContext)
    }
}