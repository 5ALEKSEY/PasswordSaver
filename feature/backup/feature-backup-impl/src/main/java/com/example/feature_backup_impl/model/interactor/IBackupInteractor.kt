package com.example.feature_backup_impl.model.interactor

import java.io.File

interface IBackupInteractor {
    suspend fun save()
    suspend fun clear()
    suspend fun getReadableInfo(): ReadableBackupInfo?
    suspend fun getFile(): File?
    suspend fun hasDataToBackup(): Boolean
}

data class ReadableBackupInfo(
    val passwordsSize: Int,
    val accountsSize: Int,
    val lastUpdateTimestamp: Long,
    val sizeInBytes: Long,
)