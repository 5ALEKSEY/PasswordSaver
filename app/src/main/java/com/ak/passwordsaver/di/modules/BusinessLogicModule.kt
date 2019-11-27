package com.ak.passwordsaver.di.modules

import com.ak.passwordsaver.presentation.screens.passwordmanage.logic.IAddNewPasswordInteractor
import com.ak.passwordsaver.presentation.screens.passwordmanage.logic.AddNewPasswordInteractorImpl
import com.ak.passwordsaver.presentation.screens.passwords.logic.IPasswordsListInteractor
import com.ak.passwordsaver.presentation.screens.passwords.logic.PasswordsListInteractorImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface BusinessLogicModule {

    //------------------------------------------- Add password ---------------------------------------------------------
    @Binds
    @Singleton
    fun provideAddNewPasswordInteractor(addNewPasswordInteractorImpl: AddNewPasswordInteractorImpl): IAddNewPasswordInteractor
    //------------------------------------------------------------------------------------------------------------------

    //------------------------------------------- Passwords list -------------------------------------------------------
    @Binds
    @Singleton
    fun providePasswordsListInteractor(passwordsListInteractorImpl: PasswordsListInteractorImpl): IPasswordsListInteractor
    //------------------------------------------------------------------------------------------------------------------
}