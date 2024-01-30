package com.example.feature_backup_impl.model.creator

import android.net.Uri
import com.example.feature_backup_impl.model.BackupInfo

interface IBackupCreator {
    suspend fun fromCurrentData(): BackupInfo?
    suspend fun fromJsonString(backupJson: String): BackupInfo?
    suspend fun fromFileUri(fileUri: Uri): BackupInfo?
}