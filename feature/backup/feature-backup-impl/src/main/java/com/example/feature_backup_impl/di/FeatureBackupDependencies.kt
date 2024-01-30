package com.example.feature_backup_impl.di

import com.ak.core_repo_api.intefaces.account.IAccountsRepository
import com.ak.core_repo_api.intefaces.password.IPasswordsRepository
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.feature_appupdate_api.interfaces.IFeaturesUpdateManager

interface FeatureBackupDependencies {
    fun needsPasswordsRepository(): IPasswordsRepository
    fun needsAccountsRepository(): IAccountsRepository
    fun needsResourceManager(): IResourceManager
    fun needsFeaturesUpdateManager(): IFeaturesUpdateManager
}