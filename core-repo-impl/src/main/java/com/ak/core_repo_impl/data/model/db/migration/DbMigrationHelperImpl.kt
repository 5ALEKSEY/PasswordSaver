package com.ak.core_repo_impl.data.model.db.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ak.core_repo_impl.BuildConfig
import com.ak.core_repo_impl.data.model.db.entities.AccountDBEntity
import javax.inject.Inject

class DbMigrationHelperImpl @Inject constructor() : IDbMigrationHelper {

    companion object {
        // version increment is always = 1
        private const val PASSWORDS_TABLE_VERSION = 1
        private const val ACCOUNTS_TABLE_VERSION = BuildConfig.DATA_BASE_VERSION
    }

    override fun getListOfDbMigrations(): List<Migration> {
        val migrationsList = mutableListOf<Migration>()

        // added accounts table (from 1 to 2 version)
        val migration1 = object : Migration(PASSWORDS_TABLE_VERSION, ACCOUNTS_TABLE_VERSION) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `${AccountDBEntity.TABLE_NAME}` " +
                                         "(" +
                                         "`${AccountDBEntity.COLUMN_ACCOUNT_ID}` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                         "`${AccountDBEntity.COLUMN_ACCOUNT_NAME}` TEXT NOT NULL, " +
                                         "`${AccountDBEntity.COLUMN_ACCOUNT_LOGIN}` TEXT NOT NULL, " +
                                         "`${AccountDBEntity.COLUMN_ACCOUNT_PASSWORD}` TEXT NOT NULL" +
                                         ")"
                )
            }
        }
        migrationsList.add(migration1)

        return migrationsList
    }
}