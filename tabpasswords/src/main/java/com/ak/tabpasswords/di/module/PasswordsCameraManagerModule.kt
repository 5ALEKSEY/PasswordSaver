package com.ak.tabpasswords.di.module

import android.content.Context
import android.hardware.camera2.CameraManager
import com.ak.tabpasswords.di.PasswordsTabScope
import dagger.Module
import dagger.Provides

@Module
internal class PasswordsCameraManagerModule {
    @Provides
    @PasswordsTabScope
    fun provideCameraManager(context: Context) =
        context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
}