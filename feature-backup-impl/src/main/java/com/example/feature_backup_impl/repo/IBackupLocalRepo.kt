package com.example.feature_backup_impl.repo

import java.io.File

interface IBackupLocalRepo {
    suspend fun save(json: String)
    suspend fun clear()
    suspend fun getStoredBackup(): String?
    suspend fun getSizeBytes(): Long
    suspend fun getFile(): File?
}