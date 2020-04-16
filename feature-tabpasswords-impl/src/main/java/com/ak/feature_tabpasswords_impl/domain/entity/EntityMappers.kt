package com.ak.feature_tabpasswords_impl.domain.entity

import com.ak.core_repo_api.intefaces.PasswordRepoEntity
import com.ak.feature_tabpasswords_api.interfaces.PasswordFeatureEntity

fun List<PasswordRepoEntity>.mapRepoToDomainEntitiesList(): List<PasswordDomainEntity> {
    return this.map { it.mapToDomainEntity() }
}

fun PasswordRepoEntity.mapToDomainEntity() = PasswordDomainEntity(
    this.getPasswordId(),
    this.getPasswordName(),
    this.getPasswordAvatarPath(),
    this.getPasswordContent()
)

fun List<PasswordFeatureEntity>.mapFeatureToDomainEntitiesList(): List<PasswordDomainEntity> {
    return this.map { it.mapToDomainEntity() }
}

fun PasswordFeatureEntity.mapToDomainEntity() = PasswordDomainEntity(
    this.getPasswordId(),
    this.getPasswordName(),
    this.getPasswordAvatarPath(),
    this.getPasswordContent()
)
