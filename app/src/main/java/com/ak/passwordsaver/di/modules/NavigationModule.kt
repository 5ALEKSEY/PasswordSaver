package com.ak.passwordsaver.di.modules

import com.ak.passwordsaver.appnavigator.AppNavigatorImpl
import com.ak.passwordsaver.appnavigator.IAppNavigator
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface NavigationModule {
    @Binds
    @Singleton
    fun provideAppNavigator(appNavigatorImpl: AppNavigatorImpl): IAppNavigator
}