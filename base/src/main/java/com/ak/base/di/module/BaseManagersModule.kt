package com.ak.base.di.module

import com.ak.base.managers.bitmapdecoder.BitmapDecoderManagerImpl
import com.ak.base.managers.bitmapdecoder.IBitmapDecoderManager
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface BaseManagersModule {

    @Binds
    @Singleton
    fun provideBitmapDecoderManager(bitmapDecoderManagerImpl: BitmapDecoderManagerImpl): IBitmapDecoderManager
}