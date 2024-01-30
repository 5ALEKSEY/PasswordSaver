package com.ak.feature_tabpasswords_impl.di

import android.content.Context
import com.ak.base.scopes.FeatureScope
import com.ak.core_repo_api.api.CoreRepositoryApi
import com.ak.feature_encryption_api.api.FeatureEncryptionApi
import com.ak.feature_tabpasswords_api.api.FeatureTabPasswordsApi
import com.ak.feature_tabpasswords_impl.di.modules.AppModule
import com.ak.feature_tabpasswords_impl.di.modules.DomainBusinessLogicModule
import com.ak.feature_tabpasswords_impl.di.modules.PasswordsTabManagersModule
import com.ak.feature_tabpasswords_impl.di.modules.PasswordsTabNavigationModule
import com.ak.feature_tabpasswords_impl.di.modules.TabPasswordsViewModelsModule
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.add.AddNewPasswordFragment
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.camera.CameraPickImageActivity
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.edit.EditPasswordFragment
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.ui.PhotoChooserBottomSheetDialog
import com.ak.feature_tabpasswords_impl.screens.presentation.passwords.PasswordsListFragment
import dagger.BindsInstance
import dagger.Component

@Component(
        modules = [
            AppModule::class,
            DomainBusinessLogicModule::class,
            PasswordsTabManagersModule::class,
            PasswordsTabNavigationModule::class,
            TabPasswordsViewModelsModule::class
        ],
        dependencies = [FeatureTabPasswordsDependencies::class]
)
@FeatureScope
abstract class FeatureTabPasswordsComponent : FeatureTabPasswordsApi {

    companion object {
        @Volatile
        private var featureTabPasswordsComponent: FeatureTabPasswordsComponent? = null

        fun initializeAndGet(dependencies: FeatureTabPasswordsDependencies, applicationContext: Context): FeatureTabPasswordsComponent {
            if (featureTabPasswordsComponent == null) {
                featureTabPasswordsComponent = DaggerFeatureTabPasswordsComponent.builder()
                    .injectAppContext(applicationContext)
                    .provideDependencies(dependencies)
                    .build()
            }

            return requireNotNull(featureTabPasswordsComponent) {
                "FeatureTabPasswordsComponent is null. initializeAndGet() didn't initialize it"
            }
        }

        fun clear() {
            featureTabPasswordsComponent = null
        }
    }

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun injectAppContext(context: Context): Builder
        fun provideDependencies(dependencies: FeatureTabPasswordsDependencies): Builder
        fun build(): FeatureTabPasswordsComponent
    }

    abstract fun inject(activity: CameraPickImageActivity)
    abstract fun inject(dialog: PhotoChooserBottomSheetDialog)
    abstract fun inject(fragment: PasswordsListFragment)
    abstract fun inject(fragment: EditPasswordFragment)
    abstract fun inject(fragment: AddNewPasswordFragment)

    @Component(dependencies = [CoreRepositoryApi::class, FeatureEncryptionApi::class])
    @FeatureScope
    interface FeatureTabPasswordsDependenciesComponent : FeatureTabPasswordsDependencies
}