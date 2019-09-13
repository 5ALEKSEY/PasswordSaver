package com.ak.passwordsaver.di.modules

import com.ak.passwordsaver.presentation.screens.addnew.logic.AddNewPasswordInteractor
import com.ak.passwordsaver.presentation.screens.addnew.logic.AddNewPasswordInteractorImpl
import com.ak.passwordsaver.presentation.screens.passwords.logic.PasswordsListInteractor
import com.ak.passwordsaver.presentation.screens.passwords.logic.PasswordsListInteractorImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface BusinessLogicModule {

    //------------------------------------------- Add password ---------------------------------------------------------
    @Binds
    @Singleton
    fun provideAddNewPasswordInteractor(addNewPasswordInteractorImpl: AddNewPasswordInteractorImpl): AddNewPasswordInteractor
    //------------------------------------------------------------------------------------------------------------------

    //------------------------------------------- Passwords list -------------------------------------------------------
    @Binds
    @Singleton
    fun providePasswordsListInteractor(passwordsListInteractorImpl: PasswordsListInteractorImpl): PasswordsListInteractor
    //------------------------------------------------------------------------------------------------------------------
}