package com.ak.core_repo_impl.data.model.mapper

import com.ak.core_repo_api.intefaces.PasswordRepoEntity
import com.ak.core_repo_impl.data.model.db.entities.PasswordDBEntity

fun PasswordRepoEntity.mapToDbEntity() = PasswordDBEntity(
        this.getPasswordId(),
        this.getPasswordName(),
        this.getPasswordAvatarPath(),
        this.getPasswordContent()
)

fun List<PasswordRepoEntity>.mapToDbEntitiesList(): List<PasswordDBEntity> {
    return this.map { it.mapToDbEntity() }
}