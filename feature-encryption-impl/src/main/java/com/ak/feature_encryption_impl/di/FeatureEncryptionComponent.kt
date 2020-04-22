package com.ak.feature_encryption_impl.di

import com.ak.feature_encryption_api.api.FeatureEncryptionApi
import com.ak.feature_encryption_impl.di.modules.EncryptionManagerModule
import dagger.Component
import javax.inject.Singleton

@Component(modules = [EncryptionManagerModule::class])
@Singleton
abstract class FeatureEncryptionComponent : FeatureEncryptionApi {

    companion object {
        @Volatile
        private var featureEncryptionComponent: FeatureEncryptionComponent? = null

        fun get(): FeatureEncryptionComponent {
            if (featureEncryptionComponent == null) {
                featureEncryptionComponent = DaggerFeatureEncryptionComponent.builder()
                    .build()
            }

            return featureEncryptionComponent!!
        }
    }
}