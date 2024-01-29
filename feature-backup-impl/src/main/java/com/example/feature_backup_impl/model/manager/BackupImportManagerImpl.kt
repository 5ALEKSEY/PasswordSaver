package com.example.feature_backup_impl.model.manager

import android.net.Uri
import com.ak.core_repo_api.intefaces.account.IAccountsRepository
import com.ak.core_repo_api.intefaces.password.IPasswordsRepository
import com.example.feature_backup_impl.model.creator.IBackupCreator
import com.example.feature_backup_impl.model.toRepoEntity
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BackupImportManagerImpl @Inject constructor(
    private val passwordsRepo: IPasswordsRepository,
    private val accountsRepo: IAccountsRepository,
    private val backupCreator: IBackupCreator,
) : IBackupImportManager {

    override suspend fun fromFileUri(fileUri: Uri) = withContext(Dispatchers.IO) {
        val backupInfo = backupCreator.fromFileUri(fileUri) ?: return@withContext false

        val passwordsToInsert = runCatching {
            backupInfo.passwords.map { it.toRepoEntity() }
        }.getOrDefault(emptyList())
        val accountsToInsert = runCatching {
            backupInfo.accounts.map { it.toRepoEntity() }
        }.getOrDefault(emptyList())

        var hasInsertion = false

        passwordsRepo.clearAll()
        if (passwordsRepo.addNewPasswords(passwordsToInsert)) {
            hasInsertion = true
        }

        accountsRepo.clearAll()
        if (accountsRepo.addNewAccounts(accountsToInsert)) {
            hasInsertion = true
        }

        return@withContext hasInsertion
    }
}