package com.ak.core_repo_impl.data.model.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.ak.core_repo_api.intefaces.account.AccountRepoEntity

@Entity(tableName = AccountDBEntity.TABLE_NAME)
data class AccountDBEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ACCOUNT_ID)
    var accountIdValue: Long?,
    @ColumnInfo(name = COLUMN_ACCOUNT_NAME)
    var accountNameValue: String,
    @ColumnInfo(name = COLUMN_ACCOUNT_LOGIN)
    var accountLoginValue: String,
    @ColumnInfo(name = COLUMN_ACCOUNT_PASSWORD)
    var accountPasswordValue: String,
    @ColumnInfo(name = COLUMN_ACCOUNT_PIN_TIMESTAMP)
    var accountPinTimestampValue: Long?,
) : AccountRepoEntity {

    @Ignore
    constructor(accountId: Long) : this(
        accountId,
        "",
        "",
        "",
        null,
    )

    companion object {
        const val TABLE_NAME = "Accounts"

        // column names
        const val COLUMN_ACCOUNT_ID = "account_id" // autogenerated primary key
        const val COLUMN_ACCOUNT_NAME = "account_name" // account alias
        const val COLUMN_ACCOUNT_LOGIN = "account_login" // login of account
        const val COLUMN_ACCOUNT_PASSWORD = "account_password" // password of account
        const val COLUMN_ACCOUNT_PIN_TIMESTAMP = "account_pin_tmstp" // pin timestamp
    }

    override fun getAccountId(): Long? = accountIdValue

    override fun getAccountName(): String = accountNameValue

    override fun getAccountLogin(): String = accountLoginValue

    override fun getAccountPassword(): String = accountPasswordValue

    override fun getAccountPinTimestamp(): Long? = accountPinTimestampValue
}