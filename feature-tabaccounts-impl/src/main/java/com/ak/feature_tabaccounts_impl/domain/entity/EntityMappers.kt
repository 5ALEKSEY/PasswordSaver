package com.ak.feature_tabaccounts_impl.domain.entity

import com.ak.core_repo_api.intefaces.AccountRepoEntity
import com.ak.feature_tabaccounts_api.interfaces.AccountFeatureEntity

fun List<AccountRepoEntity>.mapRepoToDomainEntitiesList(): List<AccountDomainEntity> {
    return this.map { it.mapToDomainEntity() }
}

fun AccountRepoEntity.mapToDomainEntity() = AccountDomainEntity(
        this.getAccountId(),
        this.getAccountName(),
        this.getAccountLogin(),
        this.getAccountPassword()
)

fun List<AccountFeatureEntity>.mapFeatureToDomainEntitiesList(): List<AccountDomainEntity> {
    return this.map { it.mapToDomainEntity() }
}

fun AccountFeatureEntity.mapToDomainEntity() = AccountDomainEntity(
        this.getAccountId(),
        this.getAccountName(),
        this.getAccountLogin(),
        this.getAccountPassword()
)
