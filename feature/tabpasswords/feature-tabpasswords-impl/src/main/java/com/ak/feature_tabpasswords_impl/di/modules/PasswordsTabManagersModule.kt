package com.ak.feature_tabpasswords_impl.di.modules

import com.ak.base.scopes.FeatureScope
import com.ak.feature_tabpasswords_impl.screens.logic.BitmapDecoderManagerImpl
import com.ak.feature_tabpasswords_impl.screens.logic.DataBufferManagerImpl
import com.ak.feature_tabpasswords_impl.screens.logic.IBitmapDecoderManager
import com.ak.feature_tabpasswords_impl.screens.logic.IDataBufferManager
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.camera.manager.IPSCameraManager
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.camera.manager.PSCameraManagerImpl
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.gallery.manager.IPSGalleryManager
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.gallery.manager.PSGalleryManagerImpl
import dagger.Binds
import dagger.Module

@Module
internal interface PasswordsTabManagersModule {

    @Binds
    @FeatureScope
    fun providePSGalleryManager(galleryManagerImpl: PSGalleryManagerImpl): IPSGalleryManager

    @Binds
    @FeatureScope
    fun provideDataBufferManager(dataBufferManagerImpl: DataBufferManagerImpl): IDataBufferManager

    @Binds
    @FeatureScope
    fun provideBitmapDecoderManager(bitmapDecoderManagerImpl: BitmapDecoderManagerImpl): IBitmapDecoderManager

    @Binds
    @FeatureScope
    fun providePSCameraManager(cameraManagerImpl: PSCameraManagerImpl): IPSCameraManager
}