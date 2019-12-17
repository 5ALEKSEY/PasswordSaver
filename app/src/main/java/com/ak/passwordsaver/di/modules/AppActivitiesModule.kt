package com.ak.passwordsaver.di.modules

import com.ak.passwordsaver.di.scopes.ActivityScope
import com.ak.passwordsaver.presentation.screens.auth.SecurityActivity
import com.ak.passwordsaver.presentation.screens.home.HomeActivity
import com.ak.passwordsaver.presentation.screens.passwordmanage.add.AddNewPasswordActivity
import com.ak.passwordsaver.presentation.screens.passwordmanage.camera.CameraPickImageActivity
import com.ak.passwordsaver.presentation.screens.passwordmanage.edit.EditPasswordActivity
import com.ak.passwordsaver.presentation.screens.settings.about.AboutSettingsActivity
import com.ak.passwordsaver.presentation.screens.settings.design.DesignSettingsActivity
import com.ak.passwordsaver.presentation.screens.settings.privacy.PrivacySettingsActivity
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
    fun injectDesignSettingsActivity(): DesignSettingsActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [ActivityModule::class])
    fun injectPrivacySettingsActivity(): PrivacySettingsActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [ActivityModule::class])
    fun injectCameraPickImageActivity(): CameraPickImageActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [ActivityModule::class])
    fun injectAboutSettingsActivity(): AboutSettingsActivity
}