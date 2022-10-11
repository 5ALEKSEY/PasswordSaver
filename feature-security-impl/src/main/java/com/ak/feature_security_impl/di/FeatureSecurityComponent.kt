package com.ak.feature_security_impl.di

import android.content.Context
import com.ak.base.scopes.FeatureScope
import com.ak.core_repo_api.api.CoreRepositoryApi
import com.ak.feature_security_api.api.FeatureSecurityApi
import com.ak.feature_security_impl.auth.SecurityActivity
import com.ak.feature_security_impl.di.modules.SecurityManagerModule
import com.ak.feature_security_impl.di.modules.SecurityViewModelsModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [SecurityManagerModule::class, SecurityViewModelsModule::class],
    dependencies = [FeatureSecurityDependencies::class]
)
@Singleton
abstract class FeatureSecurityComponent : FeatureSecurityApi {

    companion object {
        @Volatile
        private var featureSecurityComponent: FeatureSecurityComponent? = null

        fun initializeAndGet(dependencies: FeatureSecurityDependencies, applicationContext: Context): FeatureSecurityComponent {
            if (featureSecurityComponent == null) {
                featureSecurityComponent = DaggerFeatureSecurityComponent.builder()
                    .injectAppContext(applicationContext)
                    .provideDependencies(dependencies)
                    .build()
            }

            return requireNotNull(featureSecurityComponent) {
                "FeatureSecurityComponent is null. initializeAndGet() didn't initialize it"
            }
        }

        fun get(): FeatureSecurityComponent = if (featureSecurityComponent != null) {
            featureSecurityComponent!!
        } else {
            throw IllegalStateException("FeatureSecurityComponent is null. initializeAndGet() should be called before")
        }

    }

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun injectAppContext(context: Context): Builder
        fun provideDependencies(dependencies: FeatureSecurityDependencies): Builder
        fun build(): FeatureSecurityComponent
    }

    abstract fun inject(activity: SecurityActivity)

    @Component(dependencies = [CoreRepositoryApi::class])
    @FeatureScope
    interface FeatureSecurityDependenciesComponent : FeatureSecurityDependencies
}