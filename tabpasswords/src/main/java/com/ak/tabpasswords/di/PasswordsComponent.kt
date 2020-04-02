package com.ak.tabpasswords.di

import com.ak.tabpasswords.actionMode.PasswordsActionModePresenter
import com.ak.tabpasswords.di.module.PasswordsCameraManagerModule
import com.ak.tabpasswords.di.module.PasswordsTabActivitiesModule
import com.ak.tabpasswords.di.module.PasswordsTabFragmentsModule
import com.ak.tabpasswords.di.module.PasswordsTabManagersModule
import com.ak.tabpasswords.di.module.PasswordsTabNavigationModule
import com.ak.tabpasswords.presentation.passwordmanage.add.AddNewPasswordPresenter
import com.ak.tabpasswords.presentation.passwordmanage.camera.CameraPickImagePresenter
import com.ak.tabpasswords.presentation.passwordmanage.edit.EditPasswordPresenter
import com.ak.tabpasswords.presentation.passwords.PasswordsListPresenter
import dagger.Subcomponent

@Subcomponent(
    modules = [
        PasswordsTabManagersModule::class,
        PasswordsCameraManagerModule::class,
        PasswordsTabActivitiesModule::class,
        PasswordsTabFragmentsModule::class,
        PasswordsTabNavigationModule::class
    ]
)
@PasswordsTabScope
interface PasswordsComponent {

    fun inject(presenter: PasswordsActionModePresenter)
    fun inject(presenter: PasswordsListPresenter)
    fun inject(presenter: AddNewPasswordPresenter)
    fun inject(presenter: EditPasswordPresenter)
    fun inject(presenter: CameraPickImagePresenter)
}