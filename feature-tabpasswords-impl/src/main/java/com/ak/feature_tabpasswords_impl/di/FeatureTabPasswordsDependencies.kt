package com.ak.feature_tabpasswords_impl.di

import com.ak.core_repo_api.intefaces.IPSInternalStorageManager
import com.ak.core_repo_api.intefaces.IPasswordsRepository
import com.ak.core_repo_api.intefaces.ISettingsPreferencesManager

interface FeatureTabPasswordsDependencies {
    fun needsPasswordsRepository(): IPasswordsRepository
    fun needsSettingsPreferencesManager(): ISettingsPreferencesManager
    fun needsInternalStorageManager(): IPSInternalStorageManager
}