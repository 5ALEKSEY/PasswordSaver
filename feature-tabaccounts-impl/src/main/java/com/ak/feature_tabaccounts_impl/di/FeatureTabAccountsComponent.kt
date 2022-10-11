package com.ak.feature_tabaccounts_impl.di

import android.content.Context
import com.ak.base.scopes.FeatureScope
import com.ak.core_repo_api.api.CoreRepositoryApi
import com.ak.feature_encryption_api.api.FeatureEncryptionApi
import com.ak.feature_tabaccounts_api.api.FeatureTabAccountsApi
import com.ak.feature_tabaccounts_impl.di.modules.AccountsTabManagersModule
import com.ak.feature_tabaccounts_impl.di.modules.AccountsTabNavigationModule
import com.ak.feature_tabaccounts_impl.di.modules.DomainBusinessLogicModule
import com.ak.feature_tabaccounts_impl.di.modules.TabAccountsViewModelsModule
import com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.add.AddNewAccountFragment
import com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.edit.EditAccountFragment
import com.ak.feature_tabaccounts_impl.screens.presentation.accounts.AccountsListFragment
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [
        DomainBusinessLogicModule::class,
        AccountsTabManagersModule::class,
        AccountsTabNavigationModule::class,
        TabAccountsViewModelsModule::class
    ],
    dependencies = [FeatureTabAccountsDependencies::class]
)
@FeatureScope
abstract class FeatureTabAccountsComponent : FeatureTabAccountsApi {

    companion object {
        @Volatile
        private var featureTabAccountsComponent: FeatureTabAccountsComponent? = null

        fun initializeAndGet(dependencies: FeatureTabAccountsDependencies, applicationContext: Context): FeatureTabAccountsComponent {
            if (featureTabAccountsComponent == null) {
                featureTabAccountsComponent = DaggerFeatureTabAccountsComponent.builder()
                    .injectAppContext(applicationContext)
                    .provideDependencies(dependencies)
                    .build()
            }

            return requireNotNull(featureTabAccountsComponent) {
                "FeatureTabAccountsComponent is null. initializeAndGet() didn't initialize it"
            }
        }

        fun clear() {
            featureTabAccountsComponent = null
        }
    }

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun injectAppContext(context: Context): Builder
        fun provideDependencies(dependencies: FeatureTabAccountsDependencies): Builder
        fun build(): FeatureTabAccountsComponent
    }

    abstract fun inject(fragment: AccountsListFragment)
    abstract fun inject(fragment: EditAccountFragment)
    abstract fun inject(fragment: AddNewAccountFragment)

    @Component(dependencies = [CoreRepositoryApi::class, FeatureEncryptionApi::class])
    @FeatureScope
    interface FeatureTabAccountsDependenciesComponent : FeatureTabAccountsDependencies
}