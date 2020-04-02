package com.ak.tabpasswords.di.module

import com.ak.tabpasswords.di.PasswordsTabFragmentsScope
import com.ak.tabpasswords.presentation.passwordmanage.add.AddNewPasswordFragment
import com.ak.tabpasswords.presentation.passwordmanage.edit.EditPasswordFragment
import com.ak.tabpasswords.presentation.passwordmanage.ui.PhotoChooserBottomSheetDialog
import com.ak.tabpasswords.presentation.passwords.PasswordsListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Module(includes = [AndroidSupportInjectionModule::class])
interface PasswordsTabFragmentsModule {

    @PasswordsTabFragmentsScope
    @ContributesAndroidInjector
    fun injectPasswordsListFragment(): PasswordsListFragment

    @PasswordsTabFragmentsScope
    @ContributesAndroidInjector
    fun injectAddNewPasswordFragment(): AddNewPasswordFragment

    @PasswordsTabFragmentsScope
    @ContributesAndroidInjector
    fun injectEditPasswordFragment(): EditPasswordFragment

    @PasswordsTabFragmentsScope
    @ContributesAndroidInjector
    fun injectPhotoChooserDialog(): PhotoChooserBottomSheetDialog
}