package com.example.feature_backup_impl.model

import com.ak.core_repo_api.intefaces.account.AccountRepoEntity
import com.ak.core_repo_api.intefaces.password.PasswordRepoEntity

fun PasswordRepoEntity.toBackupData() = PasswordBackupData(
    id = getPasswordId() ?: 0,
    name = getPasswordName(),
    content = getPasswordContent(),
    pinTimestamp = getPasswordPinTimestamp(),
)

fun PasswordBackupData.toRepoEntity() = object : PasswordRepoEntity {
    override fun getPasswordId() = id
    override fun getPasswordName() = name
    override fun getPasswordAvatarPath() = ""
    override fun getPasswordContent() = content
    override fun getPasswordPinTimestamp() = pinTimestamp
}

fun AccountRepoEntity.toBackupData() = AccountBackupData(
    id = getAccountId() ?: 0,
    name = getAccountName(),
    login = getAccountLogin(),
    password = getAccountPassword(),
    pinTimestamp = getAccountPinTimestamp(),
)

fun AccountBackupData.toRepoEntity() = object : AccountRepoEntity {
    override fun getAccountId() = id
    override fun getAccountName() = name
    override fun getAccountLogin() = login
    override fun getAccountPassword() = password
    override fun getAccountPinTimestamp() = pinTimestamp
}