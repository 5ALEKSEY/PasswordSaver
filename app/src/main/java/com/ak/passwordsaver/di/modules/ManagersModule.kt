package com.ak.passwordsaver.di.modules

import com.ak.passwordsaver.model.preferences.SettingsPreferencesManager
import com.ak.passwordsaver.model.preferences.SettingsPreferencesManagerImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface ManagersModule {

    @Binds
    @Singleton
    fun provideSettingsPreferencesManager(settingsPreferencesManagerImpl: SettingsPreferencesManagerImpl): SettingsPreferencesManager
}