package com.ak.core_repo_impl.di.module

import com.ak.core_repo_api.intefaces.IAuthPreferencesManager
import com.ak.core_repo_api.intefaces.IPSInternalStorageManager
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.core_repo_api.intefaces.ISettingsPreferencesManager
import com.ak.core_repo_impl.AuthPreferencesManagerImpl
import com.ak.core_repo_impl.ResourceManagerImpl
import com.ak.core_repo_impl.SettingsPreferencesManagerImpl
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
}