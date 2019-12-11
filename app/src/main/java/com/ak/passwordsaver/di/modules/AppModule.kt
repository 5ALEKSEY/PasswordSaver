package com.ak.passwordsaver.di.modules

import android.content.Context
import android.hardware.camera2.CameraManager
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule(private val mAppContext: Context) {

    companion object {
        const val SETTINGS_PREFERENCES = "settings_preferences"
        const val AUTH_PREFERENCES = "auth_preferences"
    }

    @Provides
    @Singleton
    fun provideApplicationContext() = mAppContext

    @Provides
    @Singleton
    @Named(SETTINGS_PREFERENCES)
    fun provideAndroidPreferences(context: Context) =
        context.getSharedPreferences(SETTINGS_PREFERENCES, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    @Named(AUTH_PREFERENCES)
    fun provideAuthPreferences(context: Context) =
        context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideCameraManager(context: Context) =
        context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
}