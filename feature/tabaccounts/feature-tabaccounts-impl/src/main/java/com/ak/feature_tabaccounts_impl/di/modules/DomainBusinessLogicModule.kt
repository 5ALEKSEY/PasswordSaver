package com.ak.feature_tabaccounts_impl.di.modules

import com.ak.base.scopes.FeatureScope
import com.ak.feature_tabaccounts_api.interfaces.IAccountsInteractor
import com.ak.feature_tabaccounts_impl.domain.AccountsInteractorImpl
import dagger.Binds
import dagger.Module

@Module
interface DomainBusinessLogicModule {
    @Binds
    @FeatureScope
    fun provideAccountsInteractor(accountInteractorImpl: AccountsInteractorImpl): IAccountsInteractor
}