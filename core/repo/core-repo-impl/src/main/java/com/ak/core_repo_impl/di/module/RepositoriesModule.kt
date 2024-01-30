package com.ak.core_repo_impl.di.module

import com.ak.core_repo_api.intefaces.account.IAccountsRepository
import com.ak.core_repo_api.intefaces.password.IPasswordsRepository
import com.ak.core_repo_api.intefaces.theme.ICustomUserThemesRepository
import com.ak.core_repo_impl.account.AccountsRepositoryImpl
import com.ak.core_repo_impl.password.PasswordsRepositoryImpl
import com.ak.core_repo_impl.theme.CustomUserThemesRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface RepositoriesModule {
    @Binds
    @Singleton
    fun providePasswordsRepository(
        passwordsRepositoryImpl: PasswordsRepositoryImpl,
    ): IPasswordsRepository

    @Binds
    @Singleton
    fun provideAccountsRepository(
        accountsRepositoryImpl: AccountsRepositoryImpl,
    ): IAccountsRepository

    @Binds
    @Singleton
    fun provideCustomUserThemesRepository(
        customUserThemesRepositoryImpl: CustomUserThemesRepositoryImpl,
    ): ICustomUserThemesRepository
}