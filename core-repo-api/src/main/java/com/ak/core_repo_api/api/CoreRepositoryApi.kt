package com.ak.core_repo_api.api

import com.ak.core_repo_api.intefaces.IAuthPreferencesManager
import com.ak.core_repo_api.intefaces.IPSInternalStorageManager
import com.ak.core_repo_api.intefaces.IPasswordsRepository
import com.ak.core_repo_api.intefaces.ISettingsPreferencesManager

interface CoreRepositoryApi {
    fun providePasswordsRepository(): IPasswordsRepository
    fun provideSettingsPreferencesManager(): ISettingsPreferencesManager
    fun provideAuthPreferencesManager(): IAuthPreferencesManager
    fun provideInternalStorageManager(): IPSInternalStorageManager
}