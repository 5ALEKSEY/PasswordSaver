package com.ak.core_repo_impl.di

import android.content.Context
import com.ak.core_repo_api.api.CoreRepositoryApi
import com.ak.core_repo_impl.di.module.AppModule
import com.ak.core_repo_impl.di.module.DatabaseModule
import com.ak.core_repo_impl.di.module.DbMigrationsModule
import com.ak.core_repo_impl.di.module.ManagersModule
import com.ak.core_repo_impl.di.module.PreferencesModule
import com.ak.core_repo_impl.di.module.RepositoriesModule
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        AppModule::class,
        DatabaseModule::class,
        DbMigrationsModule::class,
        ManagersModule::class,
        PreferencesModule::class,
        RepositoriesModule::class
    ]
)
@Singleton
abstract class CoreRepositoryComponent : CoreRepositoryApi {

    companion object {
        @Volatile
        private var coreRepoComponent: CoreRepositoryComponent? = null
        private lateinit var appContext: Context

        fun get(applicationContext: Context): CoreRepositoryComponent {
            if (coreRepoComponent == null) {
                coreRepoComponent = DaggerCoreRepositoryComponent.builder()
                    .build()
                appContext = applicationContext
            }

            return coreRepoComponent!!
        }

        internal fun getAppContext(): Context {
            if (!this::appContext.isInitialized) {
                throw IllegalStateException("get(applicationContext: Context) should be called in CoreRepositoryComponent before")
            }

            return appContext
        }
    }
}