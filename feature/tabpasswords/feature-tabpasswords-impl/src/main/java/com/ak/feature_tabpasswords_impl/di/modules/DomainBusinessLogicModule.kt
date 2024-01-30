package com.ak.feature_tabpasswords_impl.di.modules

import com.ak.base.scopes.FeatureScope
import com.ak.feature_tabpasswords_api.interfaces.IPasswordsInteractor
import com.ak.feature_tabpasswords_impl.domain.PasswordsInteractorImpl
import dagger.Binds
import dagger.Module

@Module
interface DomainBusinessLogicModule {
    @Binds
    @FeatureScope
    fun providePasswordsInteractor(passwordsInteractorImpl: PasswordsInteractorImpl): IPasswordsInteractor
}