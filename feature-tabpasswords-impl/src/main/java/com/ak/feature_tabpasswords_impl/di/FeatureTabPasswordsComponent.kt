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
import com.ak.feature_tabpasswords_impl.screens.actionMode.PasswordsActionModePresenter
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.add.AddNewPasswordFragment
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.add.AddNewPasswordPresenter
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.camera.CameraPickImageActivity
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.camera.CameraPickImagePresenter
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.edit.EditPasswordFragment
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.edit.EditPasswordPresenter
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.ui.PhotoChooserBottomSheetDialog
import com.ak.feature_tabpasswords_impl.screens.presentation.passwords.PasswordsListFragment
import com.ak.feature_tabpasswords_impl.screens.presentation.passwords.PasswordsListPresenter
import dagger.BindsInstance
import dagger.Component

@Component(
        modules = [
            AppModule::class,
            DomainBusinessLogicModule::class,
            PasswordsTabManagersModule::class,
            PasswordsTabNavigationModule::class
        ],
        dependencies = [FeatureTabPasswordsDependencies::class]
)
@FeatureScope
abstract class FeatureTabPasswordsComponent : FeatureTabPasswordsApi {

    companion object {
        @Volatile
        private var featureTabPasswordsComponent: FeatureTabPasswordsComponent? = null

        fun initialize(dependencies: FeatureTabPasswordsDependencies, applicationContext: Context) {
            if (featureTabPasswordsComponent == null) {
                featureTabPasswordsComponent = DaggerFeatureTabPasswordsComponent.builder()
                    .injectAppContext(applicationContext)
                    .provideDependencies(dependencies)
                    .build()
            }
        }

        fun get(): FeatureTabPasswordsComponent = if (featureTabPasswordsComponent != null) {
            featureTabPasswordsComponent!!
        } else {
            throw IllegalStateException("FeatureTabPasswordsComponent is null. initialize() should be called before")
        }

        fun isInitialized() = featureTabPasswordsComponent != null
    }

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun injectAppContext(context: Context): Builder
        fun provideDependencies(dependencies: FeatureTabPasswordsDependencies): Builder
        fun build(): FeatureTabPasswordsComponent
    }

    fun clearComponent() {
        featureTabPasswordsComponent = null
    }

    abstract fun inject(activity: CameraPickImageActivity)

    abstract fun inject(presenter: CameraPickImagePresenter)
    abstract fun inject(presenter: PasswordsListPresenter)
    abstract fun inject(presenter: PasswordsActionModePresenter)
    abstract fun inject(presenter: AddNewPasswordPresenter)
    abstract fun inject(presenter: EditPasswordPresenter)

    abstract fun inject(dialog: PhotoChooserBottomSheetDialog)
    abstract fun inject(fragment: PasswordsListFragment)
    abstract fun inject(fragment: EditPasswordFragment)
    abstract fun inject(fragment: AddNewPasswordFragment)

    @Component(dependencies = [CoreRepositoryApi::class, FeatureEncryptionApi::class])
    @FeatureScope
    interface FeatureTabPasswordsDependenciesComponent : FeatureTabPasswordsDependencies
}