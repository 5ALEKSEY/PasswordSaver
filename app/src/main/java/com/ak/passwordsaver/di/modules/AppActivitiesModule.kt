package com.ak.passwordsaver.di.modules

import com.ak.passwordsaver.di.scopes.ActivityScope
import com.ak.passwordsaver.presentation.screens.auth.SecurityActivity
import com.ak.passwordsaver.presentation.screens.home.HomeActivity
import com.ak.passwordsaver.presentation.screens.passwordmanage.add.AddNewPasswordActivity
import com.ak.passwordsaver.presentation.screens.passwordmanage.camera.CameraPickImageActivity
import com.ak.passwordsaver.presentation.screens.passwordmanage.edit.EditPasswordActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Module(includes = [AndroidSupportInjectionModule::class])
interface AppActivitiesModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [ActivityModule::class])
    fun injectHomeActivity(): HomeActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [ActivityModule::class])
    fun injectAddNewPasswordActivity(): AddNewPasswordActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [ActivityModule::class])
    fun injectEditPasswordActivity(): EditPasswordActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [ActivityModule::class])
    fun injectSecurityActivity(): SecurityActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [ActivityModule::class])
    fun injectCameraPickImageActivity(): CameraPickImageActivity
}