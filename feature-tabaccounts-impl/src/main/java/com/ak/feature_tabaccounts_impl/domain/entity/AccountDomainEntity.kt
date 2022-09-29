package com.ak.feature_tabaccounts_impl.domain.entity

import com.ak.core_repo_api.intefaces.AccountRepoEntity
import com.ak.feature_tabaccounts_api.interfaces.AccountFeatureEntity

class AccountDomainEntity(
    var accountIdValue: Long?,
    var accountNameValue: String,
    var accountLoginValue: String,
    var accountPasswordValue: String,
    var accountPinTimestampValue: Long?,
) : AccountFeatureEntity, AccountRepoEntity {

    constructor(accountName: String, accountLogin: String, accountPassword: String) : this(
        null,
        accountName,
        accountLogin,
        accountPassword,
        null,
    )

    override fun getAccountId(): Long? = accountIdValue

    override fun getAccountName(): String = accountNameValue

    override fun getAccountLogin(): String = accountLoginValue

    override fun getAccountPassword(): String = accountPasswordValue

    override fun getAccountPinTimestamp(): Long? = accountPinTimestampValue
}