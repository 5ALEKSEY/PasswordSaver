package com.ak.passwordsaver.di.modules

import com.ak.passwordsaver.domain.passwords.IPasswordsInteractor
import com.ak.passwordsaver.domain.passwords.PasswordsInteractorImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface BusinessLogicModule {

    //------------------------------------------- Add password ---------------------------------------------------------
    @Binds
    @Singleton
    fun providePasswordsInteractorImpl(passwordsInteractorImpl: PasswordsInteractorImpl): IPasswordsInteractor
    //------------------------------------------------------------------------------------------------------------------
}