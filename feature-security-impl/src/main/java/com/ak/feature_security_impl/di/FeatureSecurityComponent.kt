package com.ak.feature_security_impl.di

import android.content.Context
import com.ak.base.scopes.FeatureScope
import com.ak.core_repo_api.api.CoreRepositoryApi
import com.ak.feature_security_api.api.FeatureSecurityApi
import com.ak.feature_security_impl.auth.SecurityActivity
import com.ak.feature_security_impl.auth.SecurityPresenter
import com.ak.feature_security_impl.di.modules.AppModule
import com.ak.feature_security_impl.di.modules.SecurityManagerModule
import dagger.Component
import javax.inject.Singleton

@Component(
        modules = [AppModule::class, SecurityManagerModule::class],
        dependencies = [FeatureSecurityDependencies::class]
)
@Singleton
abstract class FeatureSecurityComponent : FeatureSecurityApi {

    companion object {
        @Volatile
        private var featureSecurityComponent: FeatureSecurityComponent? = null
        private lateinit var appContext: Context

        fun initAndGet(dependencies: FeatureSecurityDependencies, applicationContext: Context): FeatureSecurityComponent {
            if (featureSecurityComponent == null) {
                featureSecurityComponent = DaggerFeatureSecurityComponent.builder()
                    .featureSecurityDependencies(dependencies)
                    .build()
                appContext = applicationContext
            }

            return featureSecurityComponent!!
        }

        fun get(): FeatureSecurityComponent = if (featureSecurityComponent != null) {
            featureSecurityComponent!!
        } else {
            throw IllegalStateException("FeatureSecurityComponent is null. initAndGet() should be called before")
        }

        fun getAppContext(): Context = if (this::appContext.isInitialized) {
            appContext
        } else {
            throw IllegalStateException("appContext is null. initAndGet() should be called before")
        }
    }

    abstract fun inject(activity: SecurityActivity)
    abstract fun inject(presenter: SecurityPresenter)

    @Component(dependencies = [CoreRepositoryApi::class])
    @FeatureScope
    interface FeatureSecurityDependenciesComponent : FeatureSecurityDependencies
}