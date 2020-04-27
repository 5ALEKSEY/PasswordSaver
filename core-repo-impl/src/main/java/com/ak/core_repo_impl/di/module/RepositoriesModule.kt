package com.ak.core_repo_impl.di.module

import com.ak.core_repo_api.intefaces.IAccountsRepository
import com.ak.core_repo_api.intefaces.IPasswordsRepository
import com.ak.core_repo_impl.AccountsRepositoryImpl
import com.ak.core_repo_impl.PasswordsRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface RepositoriesModule {
    @Binds
    @Singleton
    fun providePasswordsRepository(passwordsRepositoryImpl: PasswordsRepositoryImpl): IPasswordsRepository

    @Binds
    @Singleton
    fun provideAccountsRepository(accountsRepositoryImpl: AccountsRepositoryImpl): IAccountsRepository
}