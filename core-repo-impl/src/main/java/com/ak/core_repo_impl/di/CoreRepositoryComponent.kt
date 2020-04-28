package com.ak.core_repo_impl.di

import android.content.Context
import com.ak.core_repo_api.api.CoreRepositoryApi
import com.ak.core_repo_impl.di.module.DatabaseModule
import com.ak.core_repo_impl.di.module.DbMigrationsModule
import com.ak.core_repo_impl.di.module.ManagersModule
import com.ak.core_repo_impl.di.module.PreferencesModule
import com.ak.core_repo_impl.di.module.RepositoriesModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
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

        fun get(applicationContext: Context): CoreRepositoryComponent {
            if (coreRepoComponent == null) {
                coreRepoComponent = DaggerCoreRepositoryComponent.builder()
                    .injectAppContext(applicationContext)
                    .build()
            }

            return coreRepoComponent!!
        }
    }

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun injectAppContext(context: Context): Builder
        fun build(): CoreRepositoryComponent
    }
}