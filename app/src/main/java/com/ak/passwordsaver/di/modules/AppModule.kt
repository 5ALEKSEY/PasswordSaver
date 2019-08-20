package com.ak.passwordsaver.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val mAppContext: Context) {

    @Provides
    @Singleton
    fun provideApplicationContext() = mAppContext
}