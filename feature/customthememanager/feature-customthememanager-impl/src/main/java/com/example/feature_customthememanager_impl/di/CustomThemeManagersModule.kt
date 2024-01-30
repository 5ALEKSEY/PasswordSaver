package com.example.feature_customthememanager_impl.di

import com.ak.base.scopes.FeatureScope
import com.example.feature_customthememanager_impl.managetheme.mapper.CustomThemeMapperImpl
import com.example.feature_customthememanager_impl.managetheme.mapper.ICustomThemeMapper
import com.example.feature_customthememanager_impl.managetheme.simplemodifhelper.ISimpleModificationsHelper
import com.example.feature_customthememanager_impl.managetheme.simplemodifhelper.SimpleModificationsHelperImpl
import dagger.Binds
import dagger.Module

@Module
abstract class CustomThemeManagersModule {

    @Binds
    @FeatureScope
    abstract fun bindCustomThemeMapper(
        customThemeMapperImpl: CustomThemeMapperImpl,
    ): ICustomThemeMapper

    @Binds
    @FeatureScope
    abstract fun bindSimpleModificationsHelper(
        simpleModificationsHelperImpl: SimpleModificationsHelperImpl,
    ): ISimpleModificationsHelper
}