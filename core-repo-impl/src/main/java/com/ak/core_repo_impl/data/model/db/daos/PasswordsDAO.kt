package com.ak.core_repo_impl.data.model.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ak.core_repo_impl.data.model.db.entities.PasswordDBEntity
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface PasswordsDAO {

    @Query("SELECT * FROM ${PasswordDBEntity.TABLE_NAME} ORDER BY ${PasswordDBEntity.COLUMN_PASSWORD_ID} DESC")
    fun getAllPasswords(): Flowable<List<PasswordDBEntity>>

    @Query("SELECT * FROM ${PasswordDBEntity.TABLE_NAME} WHERE ${PasswordDBEntity.COLUMN_PASSWORD_ID} = :passwordId LIMIT 1")
    fun getPasswordById(passwordId: Long): Single<PasswordDBEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewPassword(vararg passwordDBEntity: PasswordDBEntity): List<Long>

    @Delete
    fun deletePasswords(vararg passwordDBEntities: PasswordDBEntity): Int

    @Update
    fun updatePasswords(vararg passwordDBEntities: PasswordDBEntity): Int
}