package com.ak.feature_tabaccounts_impl.di

import com.ak.core_repo_api.intefaces.IAccountsRepository
import com.ak.core_repo_api.intefaces.IDateAndTimeManager
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.feature_encryption_api.interfaces.IEncryptionManager

interface FeatureTabAccountsDependencies {
    fun needsAccountsRepository(): IAccountsRepository
    fun needsEncryptionManager(): IEncryptionManager
    fun needsResourceManager(): IResourceManager
    fun needsDateAndTimeManager() : IDateAndTimeManager
}