package com.ak.feature_tabaccounts_impl.domain

import com.ak.core_repo_api.intefaces.IAccountsRepository
import com.ak.feature_encryption_api.interfaces.IEncryptionManager
import com.ak.feature_tabaccounts_api.interfaces.IAccountsInteractor
import javax.inject.Inject

class AccountsInteractorImpl @Inject constructor(
    private val accountsRepository: IAccountsRepository,
    private val encryptionManager: IEncryptionManager
) : IAccountsInteractor {


}