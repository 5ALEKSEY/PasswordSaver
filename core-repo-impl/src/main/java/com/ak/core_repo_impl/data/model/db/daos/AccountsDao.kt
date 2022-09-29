package com.ak.core_repo_impl.data.model.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ak.core_repo_impl.data.model.db.entities.AccountDBEntity
import com.ak.core_repo_impl.data.model.db.entities.PasswordDBEntity
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

@Dao
interface AccountsDao {

    @Query("SELECT * FROM ${AccountDBEntity.TABLE_NAME} ORDER BY ${AccountDBEntity.COLUMN_ACCOUNT_ID} DESC")
    fun getAllAccounts(): Flowable<List<AccountDBEntity>>

    @Query("SELECT * FROM ${AccountDBEntity.TABLE_NAME} WHERE ${AccountDBEntity.COLUMN_ACCOUNT_ID} = :accountId LIMIT 1")
    fun getAccountById(accountId: Long): Single<AccountDBEntity>

    @Query("DELETE FROM ${AccountDBEntity.TABLE_NAME}")
    fun clearAccounts()

    @Query("SELECT COUNT(*) FROM ${AccountDBEntity.TABLE_NAME}")
    fun getAccountsCount(): Int

    @Query("UPDATE ${AccountDBEntity.TABLE_NAME} " +
               "SET ${AccountDBEntity.COLUMN_ACCOUNT_PIN_TIMESTAMP} = :pinnedTimestamp " +
               "WHERE ${AccountDBEntity.COLUMN_ACCOUNT_ID} = :accountId")
    fun markAccountAsPinned(accountId: Long, pinnedTimestamp: Long): Int

    @Query("UPDATE ${AccountDBEntity.TABLE_NAME} " +
               "SET ${AccountDBEntity.COLUMN_ACCOUNT_PIN_TIMESTAMP} = NULL " +
               "WHERE ${AccountDBEntity.COLUMN_ACCOUNT_ID} = :accountId")
    fun markAccountAsUnpinned(accountId: Long): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewAccount(vararg accountDbEntities: AccountDBEntity): List<Long>

    @Delete
    fun deleteAccounts(vararg accountDbEntities: AccountDBEntity): Int

    @Update
    fun updateAccounts(vararg accountDbEntity: AccountDBEntity): Int
}