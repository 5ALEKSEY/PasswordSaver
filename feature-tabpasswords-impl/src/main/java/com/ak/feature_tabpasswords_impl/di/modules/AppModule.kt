package com.ak.feature_tabpasswords_impl.di.modules

import android.content.Context
import android.hardware.camera2.CameraManager
import com.ak.base.scopes.FeatureScope
import com.ak.feature_tabpasswords_impl.di.FeatureTabPasswordsComponent
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    @FeatureScope
    fun provideAppContext(): Context {
        return FeatureTabPasswordsComponent.getAppContext()
    }

    @Provides
    @FeatureScope
    fun provideCameraManager(context: Context): CameraManager {
        return context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }
}