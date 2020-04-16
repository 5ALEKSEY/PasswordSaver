package com.ak.passwordsaver.di.modules

import android.content.Context
import com.ak.passwordsaver.PSApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun provideApplicationContext(): Context = PSApplication.appContext.applicationContext
}