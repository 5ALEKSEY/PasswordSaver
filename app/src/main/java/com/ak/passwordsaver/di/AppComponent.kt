package com.ak.passwordsaver.di

import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.di.modules.*
import com.ak.passwordsaver.presentation.screens.addneweditold.AddNewPasswordPresenter
import com.ak.passwordsaver.presentation.screens.addneweditold.camera.CameraPickImagePresenter
import com.ak.passwordsaver.presentation.screens.auth.SecurityPresenter
import com.ak.passwordsaver.presentation.screens.home.HomePresenter
import com.ak.passwordsaver.presentation.screens.passwords.PasswordsListPresenter
import com.ak.passwordsaver.presentation.screens.passwords.actionMode.PasswordsActionModePresenter
import com.ak.passwordsaver.presentation.screens.settings.SettingsPresenter
import com.ak.passwordsaver.presentation.screens.settings.design.DesignSettingsPresenter
import com.ak.passwordsaver.presentation.screens.settings.privacy.PrivacySettingsPresenter
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
    fun inject(presenter: HomePresenter)
    fun inject(presenter: PasswordsListPresenter)
    fun inject(presenter: AddNewPasswordPresenter)
    fun inject(presenter: PasswordsActionModePresenter)
    fun inject(presenter: SecurityPresenter)
    fun inject(presenter: SettingsPresenter)
    fun inject(presenter: DesignSettingsPresenter)
    fun inject(presenter: PrivacySettingsPresenter)
    fun inject(presenter: CameraPickImagePresenter)
}