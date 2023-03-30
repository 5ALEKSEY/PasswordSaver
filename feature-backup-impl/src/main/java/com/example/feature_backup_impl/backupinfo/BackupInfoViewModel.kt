package com.example.feature_backup_impl.backupinfo

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ak.base.livedata.SingleEventLiveData
import com.ak.base.viewmodel.BasePSViewModel
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.feature_backup_impl.R
import com.example.feature_backup_impl.backupinfo.adapter.BackupInfoListItemModel
import com.example.feature_backup_impl.backupinfo.adapter.BackupListItemModel
import com.example.feature_backup_impl.backupinfo.adapter.SimpleBackupActionListItemModel
import com.example.feature_backup_impl.model.interactor.IBackupInteractor
import com.example.feature_backup_impl.model.manager.IBackupImportManager
import com.example.feature_backup_impl.sizebeautifier.ISizeBeautifier
import com.example.feature_backup_impl.timeneautifier.ITimeBeautifier
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BackupInfoViewModel @Inject constructor(
    private val resourceManager: IResourceManager,
    private val interactor: IBackupInteractor,
    private val sizeBeautifier: ISizeBeautifier,
    private val timeBeautifier: ITimeBeautifier,
    private val backupImportManager: IBackupImportManager,
) : BasePSViewModel() {

    private val backupListStateLiveData = MutableLiveData<BackupListState>()
    private val shareBackupLiveData = SingleEventLiveData<File>()

    fun subscribeToBackupState(): LiveData<BackupListState> = backupListStateLiveData
    fun subscribeToShareBackup(): LiveData<File> = shareBackupLiveData

    fun onSimpleBackupActionClicked(itemId: Int) {
        when (itemId) {
            CLEAR_BACKUP_ITEM_ID -> clearBackup()
            else -> {
                // no op
            }
        }
    }

    fun shareBackup() {
        viewModelScope.launch {
            interactor.getFile()?.let { shareBackupLiveData.value = it }
        }
    }

    fun refreshBackup() {
        saveBackup()
    }

    fun saveBackup() {
        viewModelScope.launch {
            interactor.save()
            loadBackupListItems()
        }
    }

    fun loadBackupListItems() {
        viewModelScope.launch {
            val backupListState = obtainBackupListState(items = loadBackupListItemsInternal())
            backupListStateLiveData.value = backupListState
        }
    }

    fun importBackupWithFile(backupFileUri: Uri) {
        viewModelScope.launch {
            backupImportManager.fromFileUri(backupFileUri)
            saveBackup()
        }
    }

    private fun clearBackup() {
        viewModelScope.launch {
            interactor.clear()
            loadBackupListItems()
        }
    }

    private suspend fun loadBackupListItemsInternal() = withContext(Dispatchers.IO) {
        val backupInfo = interactor.getReadableInfo() ?: return@withContext emptyList() // early exit
        val backupInfoItem = BackupInfoListItemModel(
            BACKUP_INFO_ITEM_ID,
            backupInfo.passwordsSize,
            backupInfo.accountsSize,
            timeBeautifier.beautifyTimestamp(backupInfo.lastUpdateTimestamp),
            sizeBeautifier.beautifiedBytesToString(backupInfo.sizeInBytes),
        )

        val clearBackupItem = SimpleBackupActionListItemModel(
            CLEAR_BACKUP_ITEM_ID,
            resourceManager.getString(R.string.backup_list_clear_title),
        )

        listOfNotNull(backupInfoItem, clearBackupItem)
    }

    private suspend fun obtainBackupListState(items: List<BackupListItemModel>) = withContext(Dispatchers.IO) {
        when {
            items.isNotEmpty() -> BackupListState.BackupExists(items)
            !interactor.hasDataToBackup() -> BackupListState.NoDataToBackup
            else -> BackupListState.NoBackup
        }
    }

    private companion object {
        const val BACKUP_INFO_ITEM_ID = 0
        const val CLEAR_BACKUP_ITEM_ID = 1
    }
}