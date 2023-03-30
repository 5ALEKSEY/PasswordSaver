package com.example.feature_backup_impl.di

import com.ak.core_repo_api.intefaces.IAccountsRepository
import com.ak.core_repo_api.intefaces.IPasswordsRepository
import com.ak.core_repo_api.intefaces.IResourceManager

interface FeatureBackupDependencies {
    fun needsPasswordsRepository(): IPasswordsRepository
    fun needsAccountsRepository(): IAccountsRepository
    fun needsResourceManager(): IResourceManager
}