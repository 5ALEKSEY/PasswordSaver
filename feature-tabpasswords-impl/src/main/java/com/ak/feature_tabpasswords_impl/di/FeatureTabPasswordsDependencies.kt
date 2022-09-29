package com.ak.feature_tabpasswords_impl.di

import com.ak.core_repo_api.intefaces.IDateAndTimeManager
import com.ak.core_repo_api.intefaces.IPSInternalStorageManager
import com.ak.core_repo_api.intefaces.IPasswordsRepository
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.core_repo_api.intefaces.ISettingsPreferencesManager
import com.ak.feature_encryption_api.interfaces.IEncryptionManager

interface FeatureTabPasswordsDependencies {
    fun needsPasswordsRepository(): IPasswordsRepository
    fun needsSettingsPreferencesManager(): ISettingsPreferencesManager
    fun needsInternalStorageManager(): IPSInternalStorageManager
    fun needsEncryptionManager(): IEncryptionManager
    fun needsResourceManager(): IResourceManager
    fun needsDateAndTimeManager(): IDateAndTimeManager
}