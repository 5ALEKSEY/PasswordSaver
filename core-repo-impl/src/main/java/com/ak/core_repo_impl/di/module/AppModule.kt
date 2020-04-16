package com.ak.core_repo_impl.di.module

import android.content.Context
import com.ak.core_repo_impl.di.CoreRepositoryComponent
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideAppContext(): Context {
        return CoreRepositoryComponent.getAppContext()
    }
}