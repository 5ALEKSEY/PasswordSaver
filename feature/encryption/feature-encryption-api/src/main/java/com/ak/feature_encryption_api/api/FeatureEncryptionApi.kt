package com.ak.feature_encryption_api.api

import com.ak.feature_encryption_api.interfaces.IEncryptionManager

interface FeatureEncryptionApi {
    fun provideEncryptionUseCase(): IEncryptionManager
}