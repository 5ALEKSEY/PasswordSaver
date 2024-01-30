package com.example.feature_backup_impl.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

private const val DB_VERSION_KEY = "db_version"
private const val BACKUP_VERSION_KEY = "backup_version"
private const val CREATE_BACKUP_KEY = "create_tmstp"
private const val PASSWORDS_KEY = "passwords"
private const val ACCOUNTS_KEY = "accounts"

private const val PASSWORD_ID_KEY = "password_id"
private const val PASSWORD_NAME_KEY = "password_name"
private const val PASSWORD_CONTENT_KEY = "password_content"
private const val PASSWORD_PIN_TIMESTAMP_KEY = "password_pin_tmstp"

private const val ACCOUNT_ID_KEY = "account_id"
private const val ACCOUNT_NAME_KEY = "account_name"
private const val ACCOUNT_LOGIN_KEY = "account_login"
private const val ACCOUNT_PASSWORD_KEY = "account_password"
private const val ACCOUNT_PIN_TIMESTAMP_KEY = "account_pin_tmstp"

@Serializable
data class BackupInfo(
    @SerialName(DB_VERSION_KEY)
    val dbVersion: Int,
    @SerialName(BACKUP_VERSION_KEY)
    val backupVersion: Int,
    @SerialName(CREATE_BACKUP_KEY)
    val createBackupTimestamp: Long,
    @SerialName(PASSWORDS_KEY)
    val passwords: List<PasswordBackupData> = emptyList(),
    @SerialName(ACCOUNTS_KEY)
    val accounts: List<AccountBackupData> = emptyList(),
)

@Serializable
data class PasswordBackupData(
    @SerialName(PASSWORD_ID_KEY)
    val id: Long,
    @SerialName(PASSWORD_NAME_KEY)
    val name: String,
    @SerialName(PASSWORD_CONTENT_KEY)
    val content: String,
    @SerialName(PASSWORD_PIN_TIMESTAMP_KEY)
    val pinTimestamp: Long? = null,
)

@Serializable
data class AccountBackupData(
    @SerialName(ACCOUNT_ID_KEY)
    val id: Long,
    @SerialName(ACCOUNT_NAME_KEY)
    val name: String,
    @SerialName(ACCOUNT_LOGIN_KEY)
    val login: String,
    @SerialName(ACCOUNT_PASSWORD_KEY)
    val password: String,
    @SerialName(ACCOUNT_PIN_TIMESTAMP_KEY)
    val pinTimestamp: Long? = null,
)