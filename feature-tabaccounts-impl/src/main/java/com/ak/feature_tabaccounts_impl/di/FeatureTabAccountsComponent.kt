package com.ak.feature_tabaccounts_impl.di

import android.content.Context
import com.ak.base.scopes.FeatureScope
import com.ak.core_repo_api.api.CoreRepositoryApi
import com.ak.feature_encryption_api.api.FeatureEncryptionApi
import com.ak.feature_tabaccounts_api.api.FeatureTabAccountsApi
import com.ak.feature_tabaccounts_impl.di.modules.AccountsTabManagersModule
import com.ak.feature_tabaccounts_impl.di.modules.AccountsTabNavigationModule
import com.ak.feature_tabaccounts_impl.di.modules.AppModule
import com.ak.feature_tabaccounts_impl.di.modules.DomainBusinessLogicModule
import com.ak.feature_tabaccounts_impl.screens.actionMode.AccountsActionModePresenter
import com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.add.AddNewAccountFragment
import com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.add.AddNewAccountPresenter
import com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.edit.EditAccountFragment
import com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.edit.EditAccountPresenter
import com.ak.feature_tabaccounts_impl.screens.presentation.accounts.AccountsListFragment
import com.ak.feature_tabaccounts_impl.screens.presentation.accounts.AccountsListPresenter
import dagger.Component

@Component(
        modules = [
            AppModule::class,
            DomainBusinessLogicModule::class,
            AccountsTabManagersModule::class,
            AccountsTabNavigationModule::class
        ],
        dependencies = [FeatureTabAccountsDependencies::class]
)
@FeatureScope
abstract class FeatureTabAccountsComponent : FeatureTabAccountsApi {

    companion object {
        @Volatile
        private var featureTabAccountsComponent: FeatureTabAccountsComponent? = null
        private var appContext: Context? = null

        fun initialize(dependencies: FeatureTabAccountsDependencies, applicationContext: Context) {
            if (featureTabAccountsComponent == null) {
                featureTabAccountsComponent = DaggerFeatureTabAccountsComponent.builder()
                    .featureTabAccountsDependencies(dependencies)
                    .build()
                appContext = applicationContext
            }
        }

        fun get(): FeatureTabAccountsComponent = if (featureTabAccountsComponent != null) {
            featureTabAccountsComponent!!
        } else {
            throw IllegalStateException("FeatureTabAccountsComponent is null. initialize() should be called before")
        }

        fun getAppContext(): Context = if (appContext != null) {
            appContext!!
        } else {
            throw IllegalStateException("appContext is null. initialize() should be called before")
        }

        fun isInitialized() = featureTabAccountsComponent != null
    }

    fun clearComponent() {
        featureTabAccountsComponent = null
        appContext = null
    }

    abstract fun inject(presenter: AccountsListPresenter)
    abstract fun inject(presenter: AccountsActionModePresenter)
    abstract fun inject(presenter: AddNewAccountPresenter)
    abstract fun inject(presenter: EditAccountPresenter)

    abstract fun inject(fragment: AccountsListFragment)
    abstract fun inject(fragment: EditAccountFragment)
    abstract fun inject(fragment: AddNewAccountFragment)

    @Component(dependencies = [CoreRepositoryApi::class, FeatureEncryptionApi::class])
    @FeatureScope
    interface FeatureTabAccountsDependenciesComponent : FeatureTabAccountsDependencies
}