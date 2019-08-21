package com.ak.passwordsaver.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule(private val mAppContext: Context) {

    companion object {
        const val SETTINGS_PREFERENCES = "settings_preferences"
    }

    @Provides
    @Singleton
    fun provideApplicationContext() = mAppContext

    @Provides
    @Singleton
    @Named(SETTINGS_PREFERENCES)
    fun provideAndroidPreferences(context: Context) =
        context.getSharedPreferences(SETTINGS_PREFERENCES, Context.MODE_PRIVATE)
}