package com.example.feature_backup_impl.model.interactor

import com.ak.core_repo_api.intefaces.account.IAccountsRepository
import com.ak.core_repo_api.intefaces.password.IPasswordsRepository
import com.example.feature_backup_impl.model.creator.IBackupCreator
import com.example.feature_backup_impl.repo.IBackupLocalRepo
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class BackupInteractorImpl @Inject constructor(
    private val backupLocalRepo: IBackupLocalRepo,
    private val passwordsRepo: IPasswordsRepository,
    private val accountsRepo: IAccountsRepository,
    private val backupCreator: IBackupCreator,
) : IBackupInteractor {

    override suspend fun save() = withContext(Dispatchers.IO) {
        if (!hasDataToBackup()) {
            clear()
            return@withContext // early exit
        }
        val backupInfo = backupCreator.fromCurrentData() ?: return@withContext // early exit
        val jsonString = Json.encodeToString(backupInfo)
        backupLocalRepo.save(jsonString)
    }

    override suspend fun clear() = withContext(Dispatchers.IO) {
        backupLocalRepo.clear()
    }

    override suspend fun getReadableInfo() = withContext(Dispatchers.IO) {
        val jsonString = backupLocalRepo.getStoredBackup() ?: return@withContext null // early exit
        val backupInfo = backupCreator.fromJsonString(jsonString) ?: return@withContext null // early exit

        return@withContext ReadableBackupInfo(
            backupInfo.passwords.size,
            backupInfo.accounts.size,
            backupInfo.createBackupTimestamp,
            backupLocalRepo.getSizeBytes(),
        )
    }

    override suspend fun getFile() = withContext(Dispatchers.IO) {
        backupLocalRepo.getFile()
    }

    override suspend fun hasDataToBackup() = withContext(Dispatchers.IO) {
        passwordsRepo.getPasswordsCount() > 0 || accountsRepo.getAccountsCount() > 0
    }
}