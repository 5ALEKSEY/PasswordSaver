package com.ak.core_repo_impl.data.model.db.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ak.core_repo_impl.BuildConfig
import com.ak.core_repo_impl.data.model.db.entities.AccountDBEntity
import com.ak.core_repo_impl.data.model.db.entities.customtheme.CustomUserThemeDBEntity
import com.ak.core_repo_impl.data.model.db.entities.PasswordDBEntity
import javax.inject.Inject

class DbMigrationHelperImpl @Inject constructor() : IDbMigrationHelper {

    companion object {
        // version increment is always = 1
        private const val PASSWORDS_TABLE_VERSION = 1
        private const val ACCOUNTS_TABLE_VERSION = 2
        private const val PIN_PASSWORD_AND_ACCOUNTS_VERSION = 3
        private const val CUSTOM_USER_THEMES_TABLE_VERSION = BuildConfig.DATA_BASE_VERSION
    }

    override fun getListOfDbMigrations(): List<Migration> {
        return listOfNotNull(
            migration1to2(),
            migration2to3(),
            migration3to4(),
        )
    }

    // added 'accounts' table
    private fun migration1to2() = object : Migration(
        PASSWORDS_TABLE_VERSION,
        ACCOUNTS_TABLE_VERSION,
    ) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "CREATE TABLE IF NOT EXISTS `${AccountDBEntity.TABLE_NAME}` " +
                    "(" +
                    "`${AccountDBEntity.COLUMN_ACCOUNT_ID}` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "`${AccountDBEntity.COLUMN_ACCOUNT_NAME}` TEXT NOT NULL, " +
                    "`${AccountDBEntity.COLUMN_ACCOUNT_LOGIN}` TEXT NOT NULL, " +
                    "`${AccountDBEntity.COLUMN_ACCOUNT_PASSWORD}` TEXT NOT NULL" +
                    ")"
            )
        }
    }

    // added 'pin' password/account feature
    private fun migration2to3() = object : Migration(
        ACCOUNTS_TABLE_VERSION,
        PIN_PASSWORD_AND_ACCOUNTS_VERSION,
    ) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Add 'pin timestamp' column for passwords table
            database.execSQL(
                "ALTER TABLE `${PasswordDBEntity.TABLE_NAME}` " +
                    "ADD COLUMN `${PasswordDBEntity.COLUMN_PASSWORD_PIN_TIMESTAMP}` INTEGER"
            )
            // Add 'pin timestamp' column for accounts table
            database.execSQL(
                "ALTER TABLE `${AccountDBEntity.TABLE_NAME}` " +
                    "ADD COLUMN `${AccountDBEntity.COLUMN_ACCOUNT_PIN_TIMESTAMP}` INTEGER"
            )
        }
    }

    // added 'custom users theme' table
    private fun migration3to4() = object : Migration(
        PIN_PASSWORD_AND_ACCOUNTS_VERSION,
        CUSTOM_USER_THEMES_TABLE_VERSION,
    ) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "CREATE TABLE IF NOT EXISTS `${CustomUserThemeDBEntity.TABLE_NAME}` " +
                    "(" +
                    "`${CustomUserThemeDBEntity.COLUMN_THEME_ID}` INTEGER NOT NULL PRIMARY KEY, " +
                    "`${CustomUserThemeDBEntity.COLUMN_NAME}` TEXT NOT NULL, " +
                    "`${CustomUserThemeDBEntity.COLUMN_IS_LIGHT}` INTEGER NOT NULL, " +
                    "`${CustomUserThemeDBEntity.COLUMN_COLOR_ATTRS}` TEXT NOT NULL" +
                    ")"
            )
        }
    }
}