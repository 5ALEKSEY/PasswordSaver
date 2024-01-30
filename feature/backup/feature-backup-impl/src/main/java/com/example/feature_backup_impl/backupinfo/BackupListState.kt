package com.example.feature_backup_impl.backupinfo

import com.example.feature_backup_impl.backupinfo.adapter.BackupListItemModel

sealed class BackupListState {
    object NoBackup : BackupListState()
    object NoDataToBackup : BackupListState()
    data class BackupExists(
        val items: List<BackupListItemModel>,
    ) : BackupListState()
}