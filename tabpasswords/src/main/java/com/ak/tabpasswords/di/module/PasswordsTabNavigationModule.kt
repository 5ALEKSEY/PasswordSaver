package com.ak.tabpasswords.di.module

import com.ak.tabpasswords.di.PasswordsTabScope
import com.ak.tabpasswords.navigation.inside.IPasswordsTabNavigator
import com.ak.tabpasswords.navigation.inside.PasswordsTabNavigatorImpl
import dagger.Binds
import dagger.Module

@Module
internal interface PasswordsTabNavigationModule {

    @Binds
    @PasswordsTabScope
    fun providePasswordsTabNavigator(passwordsTabNavigatorImpl: PasswordsTabNavigatorImpl): IPasswordsTabNavigator
}