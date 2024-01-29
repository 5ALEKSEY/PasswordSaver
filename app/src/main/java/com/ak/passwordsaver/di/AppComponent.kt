package com.ak.passwordsaver.di

import com.ak.core_repo_api.api.CoreRepositoryApi
import com.ak.feature_appupdate_api.api.FeatureAppUpdateApi
import com.ak.feature_security_api.api.FeatureSecurityApi
import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.di.modules.AppModule
import com.ak.passwordsaver.di.modules.MainViewModelsModule
import com.ak.passwordsaver.di.modules.NavigationModule
import com.ak.passwordsaver.di.modules.ThemesModule
import com.ak.passwordsaver.presentation.screens.home.HomeActivity
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        AppModule::class,
        NavigationModule::class,
        MainViewModelsModule::class,
        ThemesModule::class,
    ],
    dependencies = [AppComponentDependencies::class]
)
@Singleton
abstract class AppComponent {

    companion object {
        @Volatile
        private var appComponent: AppComponent? = null

        fun get() = if (appComponent != null) {
            appComponent!!
        } else {
            throw IllegalStateException("AppComponent is null. initialize() should be called before")
        }

        fun initialize(dependencies: AppComponentDependencies): AppComponent {
            if (appComponent == null) {
                appComponent = DaggerAppComponent.builder()
                    .appModule(AppModule())
                    .appComponentDependencies(dependencies)
                    .build()
            }

            return appComponent!!
        }
    }

    abstract fun inject(app: PSApplication)
    abstract fun inject(activity: HomeActivity)

    @Component(dependencies = [CoreRepositoryApi::class, FeatureAppUpdateApi::class, FeatureSecurityApi::class])
    @Singleton
    interface AppComponentDependenciesComponent : AppComponentDependencies
}