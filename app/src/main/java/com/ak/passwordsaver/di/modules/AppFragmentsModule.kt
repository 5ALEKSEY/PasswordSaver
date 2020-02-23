package com.ak.passwordsaver.di.modules

import com.ak.passwordsaver.di.scopes.FragmentScope
import com.ak.passwordsaver.presentation.screens.passwordmanage.add.AddNewPasswordFragment
import com.ak.passwordsaver.presentation.screens.passwordmanage.edit.EditPasswordFragment
import com.ak.passwordsaver.presentation.screens.passwordmanage.ui.PhotoChooserBottomSheetDialog
import com.ak.passwordsaver.presentation.screens.passwords.PasswordsListFragment
import com.ak.passwordsaver.presentation.screens.settings.SettingsFragment
import com.ak.passwordsaver.presentation.screens.settings.about.AboutSettingsFragment
import com.ak.passwordsaver.presentation.screens.settings.design.DesignSettingsFragment
import com.ak.passwordsaver.presentation.screens.settings.privacy.PrivacySettingsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Module(includes = [AndroidSupportInjectionModule::class])
interface AppFragmentsModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [FragmentModule::class])
    fun injectPasswordsListFragment(): PasswordsListFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [FragmentModule::class])
    fun injectSettingsFragment(): SettingsFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [FragmentModule::class])
    fun injectDesignSettingsFragment(): DesignSettingsFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [FragmentModule::class])
    fun injectPrivacySettingsFragment(): PrivacySettingsFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [FragmentModule::class])
    fun injectAboutSettingsFragment(): AboutSettingsFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [FragmentModule::class])
    fun injectAddNewPasswordFragment(): AddNewPasswordFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [FragmentModule::class])
    fun injectEditPasswordFragment(): EditPasswordFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [FragmentModule::class])
    fun injectPhotoChooserDialog(): PhotoChooserBottomSheetDialog
}