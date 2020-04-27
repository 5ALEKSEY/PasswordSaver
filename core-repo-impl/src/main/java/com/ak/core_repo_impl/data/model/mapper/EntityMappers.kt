package com.ak.core_repo_impl.data.model.mapper

import com.ak.core_repo_api.intefaces.AccountRepoEntity
import com.ak.core_repo_api.intefaces.PasswordRepoEntity
import com.ak.core_repo_impl.data.model.db.entities.AccountDBEntity
import com.ak.core_repo_impl.data.model.db.entities.PasswordDBEntity

fun PasswordRepoEntity.mapToDbEntity() = PasswordDBEntity(
        this.getPasswordId(),
        this.getPasswordName(),
        this.getPasswordAvatarPath(),
        this.getPasswordContent()
)

fun List<PasswordRepoEntity>.mapToPasswordDbEntitiesList(): List<PasswordDBEntity> {
    return this.map { it.mapToDbEntity() }
}

fun AccountRepoEntity.mapToDbEntity() = AccountDBEntity(
        this.getAccountId(),
        this.getAccountName(),
        this.getAccountLogin(),
        this.getAccountPassword()
)

fun List<AccountRepoEntity>.mapToAccountDbEntitiesList(): List<AccountDBEntity> {
    return this.map { it.mapToDbEntity() }
}