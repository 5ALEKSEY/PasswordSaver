package com.ak.feature_tabaccounts_impl.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ak.base.scopes.FeatureScope
import com.ak.base.viewmodel.IViewModelAssistedFactory
import com.ak.base.viewmodel.annotation.ViewModelInjectKey
import com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.add.AddNewAccountViewModel
import com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.edit.EditAccountViewModel
import com.ak.feature_tabaccounts_impl.screens.presentation.accounts.AccountsActionModeViewModel
import com.ak.feature_tabaccounts_impl.screens.presentation.accounts.AccountsListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Named

@Module
abstract class TabAccountsViewModelsModule {

    companion object {
        const val ACCOUNTS_VIEW_MODELS_FACTORY_KEY = "accounts_view_models_factory_key"
    }

    @Binds
    @FeatureScope
    @Named(ACCOUNTS_VIEW_MODELS_FACTORY_KEY)
    abstract fun bindViewModelFactory(factory: AccountsViewModelsFactoryComposite): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @FeatureScope
    @ViewModelInjectKey(AccountsListViewModel::class)
    abstract fun bindAccountsListViewModelAssistedFactory(
        factory: AccountsListViewModelAssistedFactory
    ): IViewModelAssistedFactory<out ViewModel>

    @Binds
    @IntoMap
    @FeatureScope
    @ViewModelInjectKey(AccountsActionModeViewModel::class)
    abstract fun bindAccountsActionModeViewModelAssistedFactory(
        factory: AccountsActionModeViewModelAssistedFactory
    ): IViewModelAssistedFactory<out ViewModel>

    @Binds
    @IntoMap
    @FeatureScope
    @ViewModelInjectKey(EditAccountViewModel::class)
    abstract fun bindEditAccountViewModelAssistedFactory(
        factory: EditAccountViewModelAssistedFactory
    ): IViewModelAssistedFactory<out ViewModel>

    @Binds
    @IntoMap
    @FeatureScope
    @ViewModelInjectKey(AddNewAccountViewModel::class)
    abstract fun bindAddNewAccountViewModelAssistedFactory(
        factory: AddNewAccountViewModelAssistedFactory
    ): IViewModelAssistedFactory<out ViewModel>
}