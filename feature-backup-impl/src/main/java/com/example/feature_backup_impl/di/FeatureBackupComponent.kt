package com.example.feature_backup_impl.di

import android.content.Context
import com.ak.base.scopes.FeatureScope
import com.ak.core_repo_api.api.CoreRepositoryApi
import com.example.feature_backup_api.FeatureBackupApi
import com.example.feature_backup_impl.backupinfo.BackupInfoFragment
import com.example.feature_backup_impl.di.repo.BackupInfoModule
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [
        BackupViewModelsModule::class,
        BackupInfoModule::class,
    ],
    dependencies = [FeatureBackupDependencies::class]
)
@FeatureScope
abstract class FeatureBackupComponent : FeatureBackupApi {

    companion object {
        @Volatile
        private var featureBackupComponent: FeatureBackupComponent? = null

        fun initializeAndGet(dependencies: FeatureBackupDependencies, applicationContext: Context): FeatureBackupComponent {
            if (featureBackupComponent == null) {
                featureBackupComponent = DaggerFeatureBackupComponent.builder()
                    .injectAppContext(applicationContext)
                    .provideDependencies(dependencies)
                    .build()
            }

            return requireNotNull(featureBackupComponent) {
                "FeatureBackupComponent is null. initializeAndGet() didn't initialize it"
            }
        }

        fun clear() {
            featureBackupComponent = null
        }
    }

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun injectAppContext(context: Context): Builder
        fun provideDependencies(dependencies: FeatureBackupDependencies): Builder
        fun build(): FeatureBackupComponent
    }

    abstract fun inject(fragment: BackupInfoFragment)

    @Component(dependencies = [CoreRepositoryApi::class])
    @FeatureScope
    interface FeatureBackupDependenciesComponent : FeatureBackupDependencies
}