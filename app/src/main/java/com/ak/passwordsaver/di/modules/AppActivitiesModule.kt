package com.ak.passwordsaver.di.modules

import com.ak.passwordsaver.auth.SecurityActivity
import com.ak.passwordsaver.di.scopes.ActivityScope
import com.ak.passwordsaver.presentation.screens.home.HomeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Module(
    includes = [
        AndroidSupportInjectionModule::class
    ]
)
interface AppActivitiesModule {

    @ActivityScope
    @ContributesAndroidInjector()
    fun injectHomeActivity(): HomeActivity

    @ActivityScope
    @ContributesAndroidInjector
    fun injectSecurityActivity(): SecurityActivity
}