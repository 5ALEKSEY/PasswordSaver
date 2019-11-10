package com.ak.passwordsaver.di.modules

import com.ak.passwordsaver.model.preferences.SettingsPreferencesManager
import com.ak.passwordsaver.model.preferences.SettingsPreferencesManagerImpl
import com.ak.passwordsaver.presentation.base.managers.bitmapdecoder.BitmapDecoderManagerImpl
import com.ak.passwordsaver.presentation.base.managers.bitmapdecoder.IBitmapDecoderManager
import com.ak.passwordsaver.presentation.screens.addnew.camera.manager.IPSCameraManager
import com.ak.passwordsaver.presentation.screens.addnew.camera.manager.PSCameraManagerImpl
import com.ak.passwordsaver.presentation.screens.addnew.gallery.manager.IPSGalleryManager
import com.ak.passwordsaver.presentation.screens.addnew.gallery.manager.PSGalleryManagerImpl
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

    @Binds
    @Singleton
    fun providePSCameraManager(cameraManagerImpl: PSCameraManagerImpl): IPSCameraManager

    @Binds
    @Singleton
    fun providePSGelleryManager(galleryManagerImpl: PSGalleryManagerImpl): IPSGalleryManager
}