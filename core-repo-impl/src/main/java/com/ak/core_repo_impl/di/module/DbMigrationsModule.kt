package com.ak.core_repo_impl.di.module

import com.ak.core_repo_impl.data.model.db.migration.DbMigrationHelperImpl
import com.ak.core_repo_impl.data.model.db.migration.IDbMigrationHelper
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface DbMigrationsModule {
    @Binds
    @Singleton
    fun providePsDbMigrationHelper(dbMigrationHelperImpl: DbMigrationHelperImpl): IDbMigrationHelper
}