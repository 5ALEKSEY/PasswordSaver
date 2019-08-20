package com.ak.passwordsaver.di.modules

import com.ak.passwordsaver.di.scopes.FragmentScope
import com.ak.passwordsaver.presentation.screens.passwords.PasswordsListFragment
import com.ak.passwordsaver.presentation.screens.settings.SettingsFragment
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
}