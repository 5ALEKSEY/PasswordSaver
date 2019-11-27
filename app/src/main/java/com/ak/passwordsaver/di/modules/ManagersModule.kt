package com.ak.passwordsaver.di.modules

import com.ak.passwordsaver.model.internalstorage.IPSInternalStorageManager
import com.ak.passwordsaver.model.internalstorage.PSInternalStorageManagerImpl
import com.ak.passwordsaver.model.preferences.SettingsPreferencesManager
import com.ak.passwordsaver.model.preferences.SettingsPreferencesManagerImpl
import com.ak.passwordsaver.presentation.base.managers.bitmapdecoder.BitmapDecoderManagerImpl
import com.ak.passwordsaver.presentation.base.managers.bitmapdecoder.IBitmapDecoderManager
import com.ak.passwordsaver.presentation.screens.addneweditold.camera.manager.IPSCameraManager
import com.ak.passwordsaver.presentation.screens.addneweditold.camera.manager.PSCameraManagerImpl
import com.ak.passwordsaver.presentation.screens.addneweditold.gallery.manager.IPSGalleryManager
import com.ak.passwordsaver.presentation.screens.addneweditold.gallery.manager.PSGalleryManagerImpl
import com.ak.passwordsaver.presentation.screens.passwords.logic.DataBufferManagerImpl
import com.ak.passwordsaver.presentation.screens.passwords.logic.IDataBufferManager
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

    @Binds
    @Singleton
    fun providePSInternalStorageManager(psInternalStorageManagerImpl: PSInternalStorageManagerImpl): IPSInternalStorageManager

    @Binds
    @Singleton
    fun provideDataBufferManager(dataBufferManagerImpl: DataBufferManagerImpl): IDataBufferManager
}