package com.ak.feature_encryption_impl.di.modules

import com.ak.feature_encryption_api.interfaces.IEncryptionManager
import com.ak.feature_encryption_impl.encryption.EncryptionManagerImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface EncryptionManagerModule {
    @Binds
    @Singleton
    fun provideEncryptionUseCase(encryptionManagerImpl: EncryptionManagerImpl): IEncryptionManager
}