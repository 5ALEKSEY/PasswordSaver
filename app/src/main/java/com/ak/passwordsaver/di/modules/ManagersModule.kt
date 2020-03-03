package com.ak.passwordsaver.di.modules

import com.ak.base.managers.bitmapdecoder.BitmapDecoderManagerImpl
import com.ak.base.managers.bitmapdecoder.IBitmapDecoderManager
import com.ak.domain.data.model.internalstorage.IPSInternalStorageManager
import com.ak.domain.data.model.internalstorage.PSInternalStorageManagerImpl
import com.ak.domain.preferences.auth.AuthPreferencesManagerImpl
import com.ak.domain.preferences.auth.IAuthPreferencesManager
import com.ak.domain.preferences.settings.ISettingsPreferencesManager
import com.ak.domain.preferences.settings.SettingsPreferencesManagerImpl
import com.ak.passwordsaver.presentation.base.managers.auth.IPSAuthManager
import com.ak.passwordsaver.presentation.base.managers.auth.PSAuthManagerImpl
import com.ak.passwordsaver.presentation.screens.passwordmanage.camera.manager.IPSCameraManager
import com.ak.passwordsaver.presentation.screens.passwordmanage.camera.manager.PSCameraManagerImpl
import com.ak.passwordsaver.presentation.screens.passwordmanage.gallery.manager.IPSGalleryManager
import com.ak.passwordsaver.presentation.screens.passwordmanage.gallery.manager.PSGalleryManagerImpl
import com.ak.passwordsaver.presentation.screens.passwords.logic.DataBufferManagerImpl
import com.ak.passwordsaver.presentation.screens.passwords.logic.IDataBufferManager
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface ManagersModule {

    @Binds
    @Singleton
    fun provideSettingsPreferencesManager(settingsPreferencesManagerImpl: SettingsPreferencesManagerImpl): ISettingsPreferencesManager

    @Binds
    @Singleton
    fun provideAuthPreferencesManager(authPreferencesManagerImpl: AuthPreferencesManagerImpl): IAuthPreferencesManager

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

    @Binds
    @Singleton
    fun provideAuthAppManager(authAppManager: PSAuthManagerImpl): IPSAuthManager
}