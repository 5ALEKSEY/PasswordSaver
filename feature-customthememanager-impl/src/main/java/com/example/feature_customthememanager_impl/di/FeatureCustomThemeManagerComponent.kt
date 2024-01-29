package com.example.feature_customthememanager_impl.di

import android.content.Context
import com.ak.base.scopes.FeatureScope
import com.ak.core_repo_api.api.CoreRepositoryApi
import com.example.feature_customthememanager_api.FeatureCustomThemeManagerApi
import com.example.feature_customthememanager_impl.managetheme.ManageCustomThemeFragment
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [
        CustomThemeManagerViewModelsModule::class,
        CustomThemeManagersModule::class,
    ],
    dependencies = [FeatureCustomThemeManagerDependencies::class]
)
@FeatureScope
abstract class FeatureCustomThemeManagerComponent : FeatureCustomThemeManagerApi {

    companion object {
        @Volatile
        private var component: FeatureCustomThemeManagerComponent? = null

        fun initializeAndGet(
            dependencies: FeatureCustomThemeManagerDependencies,
            applicationContext: Context,
        ): FeatureCustomThemeManagerComponent {
            if (component == null) {
                component = DaggerFeatureCustomThemeManagerComponent.builder()
                    .injectAppContext(applicationContext)
                    .provideDependencies(dependencies)
                    .build()
            }

            return requireNotNull(component) {
                "FeatureCustomThemeManagerComponent is null. initializeAndGet() didn't initialize it"
            }
        }

        fun clear() {
            component = null
        }
    }

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun injectAppContext(context: Context): Builder
        fun provideDependencies(dependencies: FeatureCustomThemeManagerDependencies): Builder
        fun build(): FeatureCustomThemeManagerComponent
    }

    abstract fun inject(fragment: ManageCustomThemeFragment)

    @Component(dependencies = [CoreRepositoryApi::class])
    @FeatureScope
    interface FeatureCustomThemeManagerDependenciesComponent : FeatureCustomThemeManagerDependencies
}