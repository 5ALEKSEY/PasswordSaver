package com.ak.settings.di

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SettingsModule(private val mAppContext: Context) {

    @Provides
    @Singleton
    fun provideApplicationContext() = mAppContext
}