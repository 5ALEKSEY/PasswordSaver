package com.ak.passwordsaver.data.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ak.passwordsaver.BuildConfig
import com.ak.passwordsaver.data.model.db.daos.PasswordsDAO
import com.ak.passwordsaver.data.model.db.entities.PasswordDBEntity

@Database(
    version = BuildConfig.DATA_BASE_VERSION,
    entities = [PasswordDBEntity::class]
)
abstract class PSDatabase : RoomDatabase() {

    abstract fun getPasswordsDao(): PasswordsDAO
}