package com.ak.passwordsaver.injector

import com.ak.core_repo_api.api.CoreRepositoryApi
import com.ak.core_repo_impl.di.CoreRepositoryComponent
import com.ak.feature_security_api.api.FeatureSecurityApi
import com.ak.feature_security_impl.di.DaggerFeatureSecurityComponent
import com.ak.feature_security_impl.di.DaggerFeatureSecurityComponent_FeatureSecurityDependenciesComponent
import com.ak.feature_tabpasswords_impl.di.DaggerFeatureTabPasswordsComponent_FeatureTabPasswordsDependenciesComponent
import com.ak.feature_tabpasswords_impl.di.FeatureTabPasswordsComponent
import com.ak.feature_tabpasswords_impl.di.FeatureTabPasswordsDependencies
import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.R
import com.ak.passwordsaver.di.AppComponent
import com.ak.passwordsaver.di.DaggerAppComponent_AppComponentDependenciesComponent

object ApplicationInjector {

    fun onDestinationIdChanged(destinationId: Int) {
        when(destinationId) {
            R.id.passwordsListFragment -> {
                initTabPasswordsFeature()
                // TODO: clear another features
            }
            R.id.settingsFragment -> {
                // TODO: init settings feature
                // TODO: clear another features
                clearTabPasswordFeature()
            }
        }
    }

    fun initAppComponent(): AppComponent {
        return AppComponent.initialize(
                DaggerAppComponent_AppComponentDependenciesComponent.builder()
                    .featureSecurityApi(initSecurityFeature())
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

    private fun clearTabPasswordFeature() {
        if (FeatureTabPasswordsComponent.isInitialized()) {
            FeatureTabPasswordsComponent.get().clearComponent()
        }
    }

    private fun initTabPasswordsDependencies(): FeatureTabPasswordsDependencies {
        return DaggerFeatureTabPasswordsComponent_FeatureTabPasswordsDependenciesComponent.builder()
            .coreRepositoryApi(initCoreRepo())
            .build()
    }

    private fun initSecurityFeature(): FeatureSecurityApi {
        return DaggerFeatureSecurityComponent.builder()
            .featureSecurityDependencies(
                    DaggerFeatureSecurityComponent_FeatureSecurityDependenciesComponent.builder()
                        .coreRepositoryApi(initCoreRepo())
                        .build()
            )
            .build()
    }

    private fun initCoreRepo(): CoreRepositoryApi {
        return CoreRepositoryComponent.get(PSApplication.appContext)
    }
}