package com.example.feature_backup_impl.model.manager

import android.net.Uri

interface IBackupImportManager {
    suspend fun fromFileUri(fileUri: Uri): Boolean // change to flow with import status
}