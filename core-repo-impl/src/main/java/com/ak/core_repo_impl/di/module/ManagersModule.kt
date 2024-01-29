package com.ak.core_repo_impl.di.module

import com.ak.core_repo_api.intefaces.preference.IAuthPreferencesManager
import com.ak.core_repo_api.intefaces.IDateAndTimeManager
import com.ak.core_repo_api.intefaces.IPSInternalStorageManager
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.core_repo_api.intefaces.preference.ISettingsPreferencesManager
import com.ak.core_repo_impl.preference.AuthPreferencesManagerImpl
import com.ak.core_repo_impl.DateAndTimeManagerImpl
import com.ak.core_repo_impl.ResourceManagerImpl
import com.ak.core_repo_impl.preference.SettingsPreferencesManagerImpl
import com.ak.core_repo_impl.data.model.internalstorage.PSInternalStorageManagerImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface ManagersModule {

    @Binds
    @Singleton
    fun provideSettingsPreferencesManager(settingsPreferencesManagerImpl: SettingsPreferencesManagerImpl): ISettingsPreferencesManager

    @Binds
    @Singleton
    fun provideAuthPreferencesManager(authPreferencesManagerImpl: AuthPreferencesManagerImpl): IAuthPreferencesManager

    @Binds
    @Singleton
    fun providePSInternalStorageManager(psInternalStorageManagerImpl: PSInternalStorageManagerImpl): IPSInternalStorageManager

    @Binds
    @Singleton
    fun provideResourceManager(resourceManagerImpl: ResourceManagerImpl): IResourceManager

    @Binds
    @Singleton
    fun provideDateAndTimeManager(dateAndTimeManagerImpl: DateAndTimeManagerImpl): IDateAndTimeManager
}