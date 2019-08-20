package com.ak.passwordsaver.di

import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.di.modules.AppActivitiesModule
import com.ak.passwordsaver.di.modules.AppFragmentsModule
import com.ak.passwordsaver.di.modules.AppModule
import com.ak.passwordsaver.di.modules.DataBaseModule
import com.ak.passwordsaver.presentation.screens.passwords.PasswordsListPresenter
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        AppModule::class,
        DataBaseModule::class,
        AppActivitiesModule::class,
        AppFragmentsModule::class]
)
@Singleton
interface AppComponent {

    fun inject(app: PSApplication)
    fun inject(presenter: PasswordsListPresenter)
}