package com.example.feature_backup_impl.repo

import android.content.Context
import com.ak.feature_backup_impl.BuildConfig
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.io.InputStreamReader
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BackupLocalRepoImpl @Inject constructor(
    appContext: Context,
) : IBackupLocalRepo {

    private val internalFilesDir = appContext.filesDir

    override suspend fun save(json: String): Unit = withContext(Dispatchers.IO) {
        createBackupDirIfNotExists()
        BufferedWriter(FileWriter(getDumpFile())).apply {
            write(json)
            close()
        }
    }

    override suspend fun clear(): Unit = withContext(Dispatchers.IO) {
        getDumpFile().delete()
    }

    override suspend fun getStoredBackup() = withContext(Dispatchers.IO) {
        if (getBackupFilesDir().exists()) {
            getDumpFromFile(getDumpFile())
        } else {
            null
        }
    }

    override suspend fun getSizeBytes() = withContext(Dispatchers.IO) {
        if (getBackupFilesDir().exists()) {
            getDumpFile().length()
        } else {
            0L
        }
    }

    override suspend fun getFile() = withContext(Dispatchers.IO) {
        val fileCandidate = getDumpFile()
        return@withContext if (fileCandidate.exists()) fileCandidate else null
    }

    private suspend fun createBackupDirIfNotExists() = withContext(Dispatchers.IO) {
        with(getBackupFilesDir()) {
            if (!exists()) mkdir()
        }
    }

    private suspend fun getDumpFromFile(file: File) = withContext(Dispatchers.IO) {
        try {
            if (file.exists()) {
                FileInputStream(file).use {
                    convertStreamToDump(InputStreamReader(it))
                }
            } else {
                null
            }
        } catch (exception: Exception) {
            null
        }
    }

    private suspend fun convertStreamToDump(inputStreamReader: InputStreamReader) = withContext(Dispatchers.IO) {
        try {
            BufferedReader(inputStreamReader).use { reader ->
                val sb = StringBuilder()
                var line: String?
                do {
                    line = reader.readLine()
                    line?.let { sb.append(it).append(LINE_SEPARATOR) }
                } while (line != null)
                sb.toString()
            }
        } catch (exception: Exception) {
            null
        }
    }

    private fun getDumpFile(): File {
        return File(
            "${internalFilesDir.absolutePath}/${BuildConfig.DB_BACKUP_DIR_NAME}",
            "${BuildConfig.DB_BACKUP_FILE_NAME}$DUMP_FILE_EXTENSION",
        )
    }

    private fun getBackupFilesDir(): File {
        return File(internalFilesDir, BuildConfig.DB_BACKUP_DIR_NAME)
    }

    private companion object {
        const val DUMP_FILE_EXTENSION = ".txt"
        const val LINE_SEPARATOR = "\n"
    }
}