package com.ak.core_repo_impl.data.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ak.core_repo_impl.BuildConfig
import com.ak.core_repo_impl.data.model.db.daos.AccountsDao
import com.ak.core_repo_impl.data.model.db.daos.PasswordsDAO
import com.ak.core_repo_impl.data.model.db.entities.AccountDBEntity
import com.ak.core_repo_impl.data.model.db.entities.PasswordDBEntity

@Database(
    version = BuildConfig.DATA_BASE_VERSION,
    entities = [PasswordDBEntity::class, AccountDBEntity::class],
    exportSchema = false,
)
abstract class PSDatabase : RoomDatabase() {

    abstract fun getPasswordsDao(): PasswordsDAO
    abstract fun getAccountsDao(): AccountsDao
}