package com.ak.passwordsaver.di.modules

import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface ManagersModule {

    @Binds
    @Singleton
    fun provideAuthAppManager(authAppManager: com.ak.passwordsaver.auth.PSAuthManagerImpl): com.ak.passwordsaver.auth.IPSAuthManager

//    @Binds
//    @Singleton
//    fun providePSInternalStorageManager(psInternalStorageManagerImpl: PSInternalStorageManagerImpl): IPSInternalStorageManager
}