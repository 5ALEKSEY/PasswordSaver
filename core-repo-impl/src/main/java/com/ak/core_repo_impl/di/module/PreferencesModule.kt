package com.ak.core_repo_impl.di.module

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class PreferencesModule {

    companion object {
        const val AUTH_PREFERENCES = "auth_preferences"
        const val SETTINGS_PREFERENCES = "settings_preferences"
    }

    @Provides
    @Singleton
    @Named(SETTINGS_PREFERENCES)
    fun provideSettingsPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(SETTINGS_PREFERENCES, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    @Named(AUTH_PREFERENCES)
    fun provideAuthPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE)
}