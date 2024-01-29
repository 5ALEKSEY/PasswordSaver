package com.ak.core_repo_impl.data.model.mapper

import com.ak.core_repo_api.intefaces.account.AccountRepoEntity
import com.ak.core_repo_api.intefaces.password.PasswordRepoEntity
import com.ak.core_repo_api.intefaces.theme.CustomUserThemeRepoEntity
import com.ak.core_repo_api.intefaces.theme.ThemeColorAttr
import com.ak.core_repo_impl.data.model.db.entities.AccountDBEntity
import com.ak.core_repo_impl.data.model.db.entities.customtheme.CustomUserThemeDBEntity
import com.ak.core_repo_impl.data.model.db.entities.PasswordDBEntity
import com.ak.core_repo_impl.theme.CustomUserThemePreStoredEntity

fun PasswordRepoEntity.mapToDbEntity() = PasswordDBEntity(
    this.getPasswordId(),
    this.getPasswordName(),
    this.getPasswordAvatarPath(),
    this.getPasswordContent(),
    this.getPasswordPinTimestamp(),
)

fun List<PasswordRepoEntity>.mapToPasswordDbEntitiesList(): List<PasswordDBEntity> {
    return this.map { it.mapToDbEntity() }
}

fun AccountRepoEntity.mapToDbEntity() = AccountDBEntity(
    this.getAccountId(),
    this.getAccountName(),
    this.getAccountLogin(),
    this.getAccountPassword(),
    this.getAccountPinTimestamp(),
)

fun List<AccountRepoEntity>.mapToAccountDbEntitiesList(): List<AccountDBEntity> {
    return this.map { it.mapToDbEntity() }
}

fun CustomUserThemeRepoEntity.mapToDbEntity() = CustomUserThemeDBEntity(
    this.getThemeId(),
    this.getName(),
    this.isLight(),
    this.getColorAttrs(),
)

fun List<CustomUserThemeRepoEntity>.mapToThemesDbEntitiesList(): List<CustomUserThemeDBEntity> {
    return this.map { it.mapToDbEntity() }
}

fun CustomUserThemeRepoEntity.mapToPreStoredEntity() = CustomUserThemePreStoredEntity(
    this.getThemeId(),
    this.getName(),
    this.isLight(),
    this.getColorAttrs(),
)

fun List<CustomUserThemeRepoEntity>.mapToPreStoredEntitiesList(): List<CustomUserThemePreStoredEntity> {
    return this.map { it.mapToPreStoredEntity() }
}

fun CustomUserThemePreStoredEntity.toRepoEntity() = object : CustomUserThemeRepoEntity {
    override fun getThemeId() = id
    override fun getName() = name
    override fun isLight() = isLight
    override fun getColorAttrs() = colorAttrs
}

fun List<CustomUserThemePreStoredEntity>.mapToRepoEntitiesList(): List<CustomUserThemeRepoEntity> {
    return this.map { it.toRepoEntity() }
}