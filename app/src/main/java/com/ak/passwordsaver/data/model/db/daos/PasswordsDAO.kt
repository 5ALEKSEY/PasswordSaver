package com.ak.passwordsaver.data.model.db.daos

import android.arch.persistence.room.*
import com.ak.passwordsaver.data.model.db.entities.PasswordDBEntity
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface PasswordsDAO {

    @Query("SELECT * FROM " + PasswordDBEntity.PASSWORD_TABLE_NAME)
    fun getAllPasswords(): Flowable<List<PasswordDBEntity>>

    @Query(
        "SELECT * FROM " + PasswordDBEntity.PASSWORD_TABLE_NAME + " WHERE "
                + PasswordDBEntity.COLUMN_PASSWORD_ID + " = :passwordId LIMIT 1"
    )
    fun getPasswordById(passwordId: Long): Single<PasswordDBEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewPassword(vararg passwordDBEntity: PasswordDBEntity): List<Long>

    @Delete
    fun deletePasswords(vararg passwordDBEntities: PasswordDBEntity): Int

    @Update
    fun updatePasswords(vararg passwordDBEntities: PasswordDBEntity): Int
}