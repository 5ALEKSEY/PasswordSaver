package com.ak.tabpasswords.di.module

import com.ak.tabpasswords.di.PasswordsTabScope
import com.ak.tabpasswords.logic.DataBufferManagerImpl
import com.ak.tabpasswords.logic.IDataBufferManager
import com.ak.tabpasswords.presentation.passwordmanage.camera.manager.IPSCameraManager
import com.ak.tabpasswords.presentation.passwordmanage.camera.manager.PSCameraManagerImpl
import com.ak.tabpasswords.presentation.passwordmanage.gallery.manager.IPSGalleryManager
import com.ak.tabpasswords.presentation.passwordmanage.gallery.manager.PSGalleryManagerImpl
import dagger.Binds
import dagger.Module

@Module
internal interface PasswordsTabManagersModule {

    @Binds
    @PasswordsTabScope
    fun providePSCameraManager(cameraManagerImpl: PSCameraManagerImpl): IPSCameraManager

    @Binds
    @PasswordsTabScope
    fun providePSGelleryManager(galleryManagerImpl: PSGalleryManagerImpl): IPSGalleryManager


    @Binds
    @PasswordsTabScope
    fun provideDataBufferManager(dataBufferManagerImpl: DataBufferManagerImpl): IDataBufferManager
}