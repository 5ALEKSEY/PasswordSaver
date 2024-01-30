package com.ak.core_repo_api.api

import com.ak.core_repo_api.intefaces.account.IAccountsRepository
import com.ak.core_repo_api.intefaces.preference.IAuthPreferencesManager
import com.ak.core_repo_api.intefaces.IPSInternalStorageManager
import com.ak.core_repo_api.intefaces.password.IPasswordsRepository
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.core_repo_api.intefaces.preference.ISettingsPreferencesManager
import com.ak.core_repo_api.intefaces.IDateAndTimeManager
import com.ak.core_repo_api.intefaces.theme.ICustomUserThemesRepository

interface CoreRepositoryApi {
    fun providePasswordsRepository(): IPasswordsRepository
    fun provideAccountsRepository(): IAccountsRepository
    fun provideCustomUserThemesRepository(): ICustomUserThemesRepository
    // TODO: Settings preferences should be provider by settings module dagger component
    fun provideSettingsPreferencesManager(): ISettingsPreferencesManager
    fun provideAuthPreferencesManager(): IAuthPreferencesManager
    fun provideInternalStorageManager(): IPSInternalStorageManager
    fun provideResourceManager(): IResourceManager
    fun provideDateAndTimeManager(): IDateAndTimeManager
}