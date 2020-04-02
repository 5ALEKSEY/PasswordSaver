package com.ak.domain.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class DomainDataModule {

    companion object {
        const val SETTINGS_PREFERENCES = "settings_preferences"
        const val AUTH_PREFERENCES = "auth_preferences"
    }

    @Provides
    @Singleton
    @Named(SETTINGS_PREFERENCES)
    fun provideSettingsPreferences(context: Context) =
        context.getSharedPreferences(SETTINGS_PREFERENCES, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    @Named(AUTH_PREFERENCES)
    fun provideAuthPreferences(context: Context) =
        context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE)
}