package com.ak.feature_tabaccounts_impl.di

import com.ak.core_repo_api.intefaces.IAccountsRepository
import com.ak.feature_encryption_api.interfaces.IEncryptionManager

interface FeatureTabAccountsDependencies {
    fun needsAccountsRepository(): IAccountsRepository
    fun needsEncryptionManager(): IEncryptionManager
}