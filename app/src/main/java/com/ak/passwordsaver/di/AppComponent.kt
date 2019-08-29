package com.ak.passwordsaver.di

import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.di.modules.*
import com.ak.passwordsaver.presentation.screens.addnew.AddNewPasswordPresenter
import com.ak.passwordsaver.presentation.screens.passwords.PasswordsListPresenter
import com.ak.passwordsaver.presentation.screens.settings.SettingsPresenter
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        AppModule::class,
        DataBaseModule::class,
        AppActivitiesModule::class,
        AppFragmentsModule::class,
        ManagersModule::class,
        BusinessLogicModule::class]
)
@Singleton
interface AppComponent {

    fun inject(app: PSApplication)
    fun inject(presenter: PasswordsListPresenter)
    fun inject(presenter: AddNewPasswordPresenter)
    fun inject(presenter: SettingsPresenter)
}