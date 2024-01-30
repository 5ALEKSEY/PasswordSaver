package com.ak.feature_tabaccounts_impl.di.modules

import com.ak.base.scopes.FeatureScope
import com.ak.feature_tabaccounts_impl.screens.logic.DataBufferManagerImpl
import com.ak.feature_tabaccounts_impl.screens.logic.IDataBufferManager
import dagger.Binds
import dagger.Module

@Module
internal interface AccountsTabManagersModule {
    @Binds
    @FeatureScope
    fun provideDataBufferManager(dataBufferManagerImpl: DataBufferManagerImpl): IDataBufferManager
}