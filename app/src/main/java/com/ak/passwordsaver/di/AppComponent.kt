package com.ak.passwordsaver.di

import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, DataBaseModule::class])
@Singleton
interface AppComponent {


}