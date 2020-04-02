package com.ak.passwordsaver.di

import com.ak.base.di.module.BaseManagersModule
import com.ak.domain.di.module.DomainBusinessLogicModule
import com.ak.domain.di.module.DomainDataModule
import com.ak.domain.di.module.DomainManagersModule
import com.ak.domain.di.module.DomainRepositoriesModule
import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.auth.SecurityPresenter
import com.ak.passwordsaver.di.modules.AppActivitiesModule
import com.ak.passwordsaver.di.modules.AppModule
import com.ak.passwordsaver.di.modules.BusinessLogicModule
import com.ak.passwordsaver.di.modules.DataBaseModule
import com.ak.passwordsaver.di.modules.ManagersModule
import com.ak.passwordsaver.di.modules.NavigationModule
import com.ak.passwordsaver.presentation.screens.home.HomePresenter
import com.ak.tabpasswords.di.PasswordsComponent
import dagger.Component
import javax.inject.Singleton


@Component(
    modules = [
        // App dagger modules
        AppModule::class,
        DataBaseModule::class,
        AppActivitiesModule::class,
        ManagersModule::class,
        BusinessLogicModule::class,
        NavigationModule::class,
        // Domain dagger modules
        DomainBusinessLogicModule::class,
        DomainDataModule::class,
        DomainManagersModule::class,
        DomainRepositoriesModule::class,
        // Base dagger modules
        BaseManagersModule::class]
)
@Singleton
interface AppComponent {
    fun initPasswordsComponent(): PasswordsComponent

    fun inject(app: PSApplication)
    fun inject(presenter: HomePresenter)
    fun inject(presenter: SecurityPresenter)
}