package com.ak.passwordsaver.di

import com.ak.core_repo_api.api.CoreRepositoryApi
import com.ak.feature_security_api.api.FeatureSecurityApi
import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.di.modules.AppModule
import com.ak.passwordsaver.di.modules.NavigationModule
import com.ak.passwordsaver.presentation.screens.home.HomeActivity
import com.ak.passwordsaver.presentation.screens.home.HomePresenter
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        AppModule::class,
        NavigationModule::class
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
            throw IllegalStateException("AppComponent is null. init() should be called before")
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
    abstract fun inject(presenter: HomePresenter)
    abstract fun inject(activity: HomeActivity)

    @Component(dependencies = [FeatureSecurityApi::class, CoreRepositoryApi::class])
    @Singleton
    interface AppComponentDependenciesComponent : AppComponentDependencies
}