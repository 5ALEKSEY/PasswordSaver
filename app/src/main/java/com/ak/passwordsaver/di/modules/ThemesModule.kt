package com.ak.passwordsaver.di.modules

import com.ak.app_theme.theme.ICustomUserThemesProvider
import com.ak.passwordsaver.userthemesprovider.CustomUserThemesProviderImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface ThemesModule {
    @Binds
    @Singleton
    fun bindCustomUserThemesProvider(
        impl: CustomUserThemesProviderImpl,
    ): ICustomUserThemesProvider
}