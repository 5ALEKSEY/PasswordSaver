package com.example.feature_backup_impl.backupinfo.adapter

interface BackupListItemModel {
    val itemId: Int

    fun areContentsTheSame(item: BackupListItemModel): Boolean
}

class BackupInfoListItemModel constructor(
    override val itemId: Int,
    val passwordsCount: Int,
    val accountsCount: Int,
    val lastUpdateTime: String,
    val backupFileSize: String,
) : BackupListItemModel {

    override fun areContentsTheSame(item: BackupListItemModel): Boolean {
        val sameItem = item as? BackupInfoListItemModel ?: return false

        return passwordsCount == sameItem.passwordsCount
            && accountsCount == sameItem.accountsCount
            && lastUpdateTime == sameItem.lastUpdateTime
            && backupFileSize == sameItem.backupFileSize
    }
}

class SimpleBackupActionListItemModel constructor(
    override val itemId: Int,
    val actionText: String,
) : BackupListItemModel {
    override fun areContentsTheSame(item: BackupListItemModel): Boolean {
        val sameItem = item as? SimpleBackupActionListItemModel ?: return false

        return actionText == sameItem.actionText
    }
}