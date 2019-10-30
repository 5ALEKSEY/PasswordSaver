package com.ak.passwordsaver.di.modules

import com.ak.passwordsaver.model.preferences.SettingsPreferencesManager
import com.ak.passwordsaver.model.preferences.SettingsPreferencesManagerImpl
import com.ak.passwordsaver.presentation.base.managers.bitmapdecoder.BitmapDecoderManagerImpl
import com.ak.passwordsaver.presentation.base.managers.bitmapdecoder.IBitmapDecoderManager
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface ManagersModule {

    @Binds
    @Singleton
    fun provideSettingsPreferencesManager(settingsPreferencesManagerImpl: SettingsPreferencesManagerImpl): SettingsPreferencesManager

    @Binds
    @Singleton
    fun provideBitmapDecoderManager(bitmapDecoderManagerImpl: BitmapDecoderManagerImpl): IBitmapDecoderManager
}