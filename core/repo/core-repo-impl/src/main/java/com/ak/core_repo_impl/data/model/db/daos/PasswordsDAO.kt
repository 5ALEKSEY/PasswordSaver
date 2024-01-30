package com.ak.core_repo_impl.data.model.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ak.core_repo_impl.data.model.db.entities.PasswordDBEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordsDAO {

    @Query("SELECT * FROM ${PasswordDBEntity.TABLE_NAME} " +
               "ORDER BY " +
               "${PasswordDBEntity.COLUMN_PASSWORD_PIN_TIMESTAMP} DESC, " +
               "${PasswordDBEntity.COLUMN_PASSWORD_ID} DESC")
    fun getAllPasswords(): Flow<List<PasswordDBEntity>>

    @Query("SELECT * FROM ${PasswordDBEntity.TABLE_NAME} " +
               "WHERE ${PasswordDBEntity.COLUMN_PASSWORD_ID} = :passwordId LIMIT 1")
    suspend fun getPasswordById(passwordId: Long): PasswordDBEntity

    @Query("DELETE FROM ${PasswordDBEntity.TABLE_NAME}")
    suspend fun clearPasswords()

    @Query("SELECT COUNT(*) FROM ${PasswordDBEntity.TABLE_NAME}")
    suspend fun getPasswordsCount(): Int

    @Query("UPDATE ${PasswordDBEntity.TABLE_NAME} " +
               "SET ${PasswordDBEntity.COLUMN_PASSWORD_PIN_TIMESTAMP} = :pinnedTimestamp " +
               "WHERE ${PasswordDBEntity.COLUMN_PASSWORD_ID} = :passwordId")
    suspend fun markPasswordAsPinned(passwordId: Long, pinnedTimestamp: Long): Int

    @Query("UPDATE ${PasswordDBEntity.TABLE_NAME} " +
               "SET ${PasswordDBEntity.COLUMN_PASSWORD_PIN_TIMESTAMP} = NULL " +
               "WHERE ${PasswordDBEntity.COLUMN_PASSWORD_ID} = :passwordId")
    suspend fun markPasswordAsUnpinned(passwordId: Long): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewPassword(vararg passwordDBEntity: PasswordDBEntity): List<Long>

    @Delete
    suspend fun deletePasswords(vararg passwordDBEntities: PasswordDBEntity): Int

    @Update
    suspend fun updatePasswords(vararg passwordDBEntities: PasswordDBEntity): Int
}