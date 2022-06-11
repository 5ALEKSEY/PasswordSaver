package com.ak.feature_tabaccounts_impl.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ak.base.scopes.FeatureScope
import com.ak.base.viewmodel.BaseViewModelFactory
import com.ak.base.viewmodel.annotation.ViewModelInjectKey
import com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.add.AddNewAccountViewModel
import com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.edit.EditAccountViewModel
import com.ak.feature_tabaccounts_impl.screens.presentation.accounts.AccountsActionModeViewModel
import com.ak.feature_tabaccounts_impl.screens.presentation.accounts.AccountsListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider

@Module
abstract class TabAccountsViewModelsModule {

    companion object {
        const val ACCOUNTS_VIEW_MODELS_FACTORY_KEY = "accounts_view_models_factory_key"
    }

    @Binds
    @FeatureScope
    @Named(ACCOUNTS_VIEW_MODELS_FACTORY_KEY)
    abstract fun bindViewModelFactory(factory: AccountsViewModelsFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @FeatureScope
    @ViewModelInjectKey(AccountsListViewModel::class)
    abstract fun injectAccountsListViewModel(viewModel: AccountsListViewModel): ViewModel

    @Binds
    @IntoMap
    @FeatureScope
    @ViewModelInjectKey(AccountsActionModeViewModel::class)
    abstract fun injectAccountsActionModeViewModel(viewModel: AccountsActionModeViewModel): ViewModel

    @Binds
    @IntoMap
    @FeatureScope
    @ViewModelInjectKey(EditAccountViewModel::class)
    abstract fun injectEditAccountViewModel(viewModel: EditAccountViewModel): ViewModel

    @Binds
    @IntoMap
    @FeatureScope
    @ViewModelInjectKey(AddNewAccountViewModel::class)
    abstract fun injectAddNewAccountViewModel(viewModel: AddNewAccountViewModel): ViewModel
}

class AccountsViewModelsFactory @Inject constructor(
    viewModels: MutableMap<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : BaseViewModelFactory(viewModels)