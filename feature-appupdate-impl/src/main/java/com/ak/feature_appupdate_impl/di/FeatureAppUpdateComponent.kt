package com.ak.feature_appupdate_impl.di

import android.content.Context
import com.ak.feature_appupdate_api.api.FeatureAppUpdateApi
import com.ak.feature_appupdate_impl.di.modules.FeatureAppUpdateManagersModule
import com.ak.feature_appupdate_impl.di.modules.PreferencesModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
        modules = [
            PreferencesModule::class,
            FeatureAppUpdateManagersModule::class
        ]
)
@Singleton
abstract class FeatureAppUpdateComponent : FeatureAppUpdateApi {

    companion object {
        @Volatile
        private var featureAppUpdateComponent: FeatureAppUpdateComponent? = null

        fun get(applicationContext: Context): FeatureAppUpdateComponent {
            if (featureAppUpdateComponent == null) {
                featureAppUpdateComponent = DaggerFeatureAppUpdateComponent.builder()
                    .injectAppContext(applicationContext)
                    .build()
            }

            return featureAppUpdateComponent!!
        }
    }

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun injectAppContext(context: Context): Builder

        fun build(): FeatureAppUpdateComponent
    }
}