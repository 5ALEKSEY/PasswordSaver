package com.ak.core_repo_impl.data.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ak.core_repo_impl.BuildConfig
import com.ak.core_repo_impl.data.model.db.daos.PasswordsDAO
import com.ak.core_repo_impl.data.model.db.entities.PasswordDBEntity

@Database(
    version = BuildConfig.DATA_BASE_VERSION,
    entities = [PasswordDBEntity::class]
)
abstract class PSDatabase : RoomDatabase() {

    abstract fun getPasswordsDao(): PasswordsDAO
}