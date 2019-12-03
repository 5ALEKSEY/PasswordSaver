package com.ak.passwordsaver.di.modules

import com.ak.passwordsaver.data.repository.passwords.IPasswordsRepository
import com.ak.passwordsaver.data.repository.passwords.PasswordsRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface RepositoriesModule {

    @Binds
    @Singleton
    fun providePasswordsRepository(passwordsRepositoryImpl: PasswordsRepositoryImpl): IPasswordsRepository
}