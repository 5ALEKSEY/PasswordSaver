package com.ak.domain.di.module

import com.ak.domain.data.model.internalstorage.IPSInternalStorageManager
import com.ak.domain.data.model.internalstorage.PSInternalStorageManagerImpl
import com.ak.domain.preferences.auth.AuthPreferencesManagerImpl
import com.ak.domain.preferences.auth.IAuthPreferencesManager
import com.ak.domain.preferences.settings.ISettingsPreferencesManager
import com.ak.domain.preferences.settings.SettingsPreferencesManagerImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface DomainManagersModule {

    @Binds
    @Singleton
    fun provideSettingsPreferencesManager(settingsPreferencesManagerImpl: SettingsPreferencesManagerImpl): ISettingsPreferencesManager

    @Binds
    @Singleton
    fun provideAuthPreferencesManager(authPreferencesManagerImpl: AuthPreferencesManagerImpl): IAuthPreferencesManager

    @Binds
    @Singleton
    fun providePSInternalStorageManager(psInternalStorageManagerImpl: PSInternalStorageManagerImpl): IPSInternalStorageManager
}