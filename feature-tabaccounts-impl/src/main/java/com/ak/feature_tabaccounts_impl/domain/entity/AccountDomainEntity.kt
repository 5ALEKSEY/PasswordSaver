package com.ak.feature_tabaccounts_impl.domain.entity

import com.ak.core_repo_api.intefaces.AccountRepoEntity
import com.ak.feature_tabaccounts_api.interfaces.AccountFeatureEntity

class AccountDomainEntity(
    var accountIdValue: Long?,
    var accountNameValue: String,
    var accountAvatarPathValue: String,
    var accountLoginValue: String,
    var accountPasswordValue: String
) : AccountFeatureEntity, AccountRepoEntity {

    override fun getAccountId(): Long? = accountIdValue

    override fun getAccountName(): String = accountNameValue

    override fun getAccountAvatarPath(): String = accountAvatarPathValue

    override fun getAccountLogin(): String = accountLoginValue

    override fun getAccountPassword(): String = accountPasswordValue
}