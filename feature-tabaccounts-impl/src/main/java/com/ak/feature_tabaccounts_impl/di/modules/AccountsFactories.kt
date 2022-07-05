package com.ak.feature_tabaccounts_impl.di.modules

import androidx.lifecycle.ViewModel
import com.ak.base.viewmodel.BaseViewModelFactory
import com.ak.base.viewmodel.IViewModelAssistedFactory
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.feature_tabaccounts_api.interfaces.IAccountsInteractor
import com.ak.feature_tabaccounts_impl.screens.logic.IDataBufferManager
import com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.add.AddNewAccountViewModel
import com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.edit.EditAccountViewModel
import com.ak.feature_tabaccounts_impl.screens.presentation.accounts.AccountsActionModeViewModel
import com.ak.feature_tabaccounts_impl.screens.presentation.accounts.AccountsListViewModel
import javax.inject.Inject

class AccountsViewModelsFactoryComposite @Inject constructor(
    viewModels: MutableMap<Class<out ViewModel>, IViewModelAssistedFactory<out ViewModel>>
) : BaseViewModelFactory(viewModels)

class AccountsListViewModelAssistedFactory @Inject constructor(
    private val accountsInteractor: IAccountsInteractor,
    private val dataBufferManager: IDataBufferManager,
    private val resourceManager: IResourceManager,
): IViewModelAssistedFactory<AccountsListViewModel> {
    override fun create(): AccountsListViewModel {
        return AccountsListViewModel(
            accountsInteractor = accountsInteractor,
            dataBufferManager = dataBufferManager,
            resourceManager = resourceManager,
        )
    }
}

class AccountsActionModeViewModelAssistedFactory @Inject constructor(
    private val passwordsInteractor: IAccountsInteractor
): IViewModelAssistedFactory<AccountsActionModeViewModel> {
    override fun create(): AccountsActionModeViewModel {
        return AccountsActionModeViewModel(passwordsInteractor)
    }
}

class EditAccountViewModelAssistedFactory @Inject constructor(
    private val passwordsInteractor: IAccountsInteractor
): IViewModelAssistedFactory<EditAccountViewModel> {
    override fun create(): EditAccountViewModel {
        return EditAccountViewModel(passwordsInteractor)
    }
}

class AddNewAccountViewModelAssistedFactory @Inject constructor(
    private val passwordsInteractor: IAccountsInteractor
): IViewModelAssistedFactory<AddNewAccountViewModel> {
    override fun create(): AddNewAccountViewModel {
        return AddNewAccountViewModel(passwordsInteractor)
    }
}