package com.ak.passwordsaver.model.db.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.ak.passwordsaver.model.db.entities.PasswordDBEntity
import io.reactivex.Single

@Dao
interface PasswordsDAO {

    @Query("SELECT * FROM " + PasswordDBEntity.PASSWORD_TABLE_NAME)
    fun getAllPasswords(): Single<List<PasswordDBEntity>>

    @Query(
        "SELECT * FROM " + PasswordDBEntity.PASSWORD_TABLE_NAME + " WHERE "
                + PasswordDBEntity.COLUMN_PASSWORD_ID + " = :passwordId LIMIT 1"
    )
    fun getPasswordById(passwordId: Long): Single<PasswordDBEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewPassword(vararg passwordDBEntity: PasswordDBEntity): List<Long>
}