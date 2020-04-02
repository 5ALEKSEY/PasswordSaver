package com.ak.domain.di.module

import com.ak.domain.passwords.IPasswordsInteractor
import com.ak.domain.passwords.PasswordsInteractorImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface DomainBusinessLogicModule {

    @Binds
    @Singleton
    fun providePasswordsInteractorImpl(passwordsInteractorImpl: PasswordsInteractorImpl): IPasswordsInteractor
}