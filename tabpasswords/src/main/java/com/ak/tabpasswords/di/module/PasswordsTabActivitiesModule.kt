package com.ak.tabpasswords.di.module

import com.ak.tabpasswords.di.PasswordsTabActivitiesScope
import com.ak.tabpasswords.presentation.passwordmanage.camera.CameraPickImageActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Module(includes = [AndroidSupportInjectionModule::class])
internal interface PasswordsTabActivitiesModule {

    @PasswordsTabActivitiesScope
    @ContributesAndroidInjector
    fun injectCameraPickImageActivity(): CameraPickImageActivity
}