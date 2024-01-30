package com.example.feature_backup_impl.model.creator

import android.content.Context
import android.net.Uri
import com.ak.core_repo_api.intefaces.account.IAccountsRepository
import com.ak.core_repo_api.intefaces.password.IPasswordsRepository
import com.ak.feature_backup_impl.BuildConfig
import com.example.feature_backup_impl.model.BackupInfo
import com.example.feature_backup_impl.model.toBackupData
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class BackupCreatorImpl @Inject constructor(
    private val appContext: Context,
    private val passwordsRepo: IPasswordsRepository,
    private val accountsRepo: IAccountsRepository,
) : IBackupCreator {

    override suspend fun fromCurrentData() = withContext(Dispatchers.IO) {
        val passwordsBackupData = runCatching {
            passwordsRepo.getAllPasswords().first().map { it.toBackupData() }
        }.getOrDefault(emptyList())
        val accountsBackupData = runCatching {
            accountsRepo.getAllAccounts().first().map { it.toBackupData() }
        }.getOrDefault(emptyList())

        if (passwordsBackupData.isEmpty() && accountsBackupData.isEmpty()) {
            return@withContext null // early exit
        }

        return@withContext BackupInfo(
            BuildConfig.DATA_BASE_VERSION,
            BuildConfig.BACKUP_VERSION,
            System.currentTimeMillis(),
            passwordsBackupData,
            accountsBackupData,
        )
    }

    override suspend fun fromJsonString(backupJson: String) = withContext(Dispatchers.IO) {
        runCatching {
            Json.decodeFromString<BackupInfo>(backupJson)
        }.getOrNull()
    }

    override suspend fun fromFileUri(fileUri: Uri) = withContext(Dispatchers.IO) {
        val jsonString = runCatching {
            readTextFromUri(fileUri)
        }.getOrNull() ?: return@withContext null // early exit

        return@withContext fromJsonString(jsonString)
    }

    private suspend fun readTextFromUri(uri: Uri) = withContext(Dispatchers.IO) {
        val stringBuilder = StringBuilder()
        appContext.contentResolver.openInputStream(uri)?.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                var line = reader.readLine()
                while (line != null) {
                    stringBuilder.append(line)
                    line = reader.readLine()
                }
            }
        }
        return@withContext stringBuilder.toString()
    }
}