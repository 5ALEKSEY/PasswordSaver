package com.ak.tabpasswords.di

import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
internal annotation class PasswordsTabScope

@Scope
@Retention(AnnotationRetention.RUNTIME)
internal annotation class PasswordsTabActivitiesScope

@Scope
@Retention(AnnotationRetention.RUNTIME)
internal annotation class PasswordsTabFragmentsScope