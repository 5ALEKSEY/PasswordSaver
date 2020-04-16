package com.ak.feature_tabpasswords_impl.di.modules

import com.ak.base.scopes.FeatureScope
import com.ak.feature_tabpasswords_impl.screens.navigation.inside.IPasswordsTabNavigator
import com.ak.feature_tabpasswords_impl.screens.navigation.inside.PasswordsTabNavigatorImpl
import dagger.Binds
import dagger.Module

@Module
internal interface PasswordsTabNavigationModule {

    @Binds
    @FeatureScope
    fun providePasswordsTabNavigator(passwordsTabNavigatorImpl: PasswordsTabNavigatorImpl): IPasswordsTabNavigator
}