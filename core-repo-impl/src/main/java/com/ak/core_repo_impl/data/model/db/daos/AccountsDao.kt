package com.ak.core_repo_impl.data.model.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ak.core_repo_impl.data.model.db.entities.AccountDBEntity
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface AccountsDao {

    @Query("SELECT * FROM ${AccountDBEntity.TABLE_NAME} ORDER BY ${AccountDBEntity.COLUMN_ACCOUNT_ID} DESC")
    fun getAllAccounts(): Flowable<List<AccountDBEntity>>

    @Query("SELECT * FROM ${AccountDBEntity.TABLE_NAME} WHERE ${AccountDBEntity.COLUMN_ACCOUNT_ID} = :accountId LIMIT 1")
    fun getAccountById(accountId: Long): Single<AccountDBEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewAccount(vararg accountDbEntities: AccountDBEntity): List<Long>

    @Delete
    fun deleteAccounts(vararg accountDbEntities: AccountDBEntity): Int

    @Update
    fun updateAccounts(vararg accountDbEntity: AccountDBEntity): Int
}