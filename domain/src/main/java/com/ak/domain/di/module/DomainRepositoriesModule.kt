package com.ak.domain.di.module

import com.ak.domain.data.repository.passwords.IPasswordsRepository
import com.ak.domain.data.repository.passwords.PasswordsRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface DomainRepositoriesModule {

    @Binds
    @Singleton
    fun providePasswordsRepository(passwordsRepositoryImpl: PasswordsRepositoryImpl): IPasswordsRepository
}