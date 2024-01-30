package com.ak.feature_tabaccounts_impl.di.modules

import com.ak.base.scopes.FeatureScope
import com.ak.feature_tabaccounts_impl.screens.navigation.inside.AccountsTabNavigatorImpl
import com.ak.feature_tabaccounts_impl.screens.navigation.inside.IAccountsTabNavigator
import dagger.Binds
import dagger.Module

@Module
internal interface AccountsTabNavigationModule {

    @Binds
    @FeatureScope
    fun provideAccountsTabNavigator(accountsTabNavigatorImpl: AccountsTabNavigatorImpl): IAccountsTabNavigator
}