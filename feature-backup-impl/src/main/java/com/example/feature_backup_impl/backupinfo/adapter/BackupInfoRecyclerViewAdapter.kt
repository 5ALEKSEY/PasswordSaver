package com.example.feature_backup_impl.backupinfo.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.uicomponents.recyclerview.CustomThemeRecyclerViewAdapter
import com.ak.app_theme.theme.uicomponents.recyclerview.CustomThemeRecyclerViewHolder
import com.ak.base.adapter.AdapterDelegatesManager
import com.example.feature_backup_impl.backupinfo.adapter.delegates.BackupInfoAdapterDelegate
import com.example.feature_backup_impl.backupinfo.adapter.delegates.SimpleBackupActionAdapterDelegate

class BackupInfoRecyclerViewAdapter constructor(
    private val shareBackupListener: () -> Unit,
    private val refreshBackupListener: () -> Unit,
    private val simpleBackupActionListener: (itemId: Int) -> Unit,
) : CustomThemeRecyclerViewAdapter<CustomThemeRecyclerViewHolder>() {

    private val backupItemsList = arrayListOf<BackupListItemModel>()
    private val adapterDelegatesManager = AdapterDelegatesManager<BackupListItemModel>()

    init {
        adapterDelegatesManager.addDelegate(
            BackupInfoAdapterDelegate(
                viewType = BACKUP_INFO_TYPE,
                shareBackupListener = shareBackupListener,
                refreshBackupListener = refreshBackupListener,
            )
        )
        adapterDelegatesManager.addDelegate(
            SimpleBackupActionAdapterDelegate(
                viewType = CLEAR_BACKUP_TYPE,
                actionClickListener = simpleBackupActionListener,
            )
        )
    }

    fun submitNewItems(newItems: List<BackupListItemModel>) {
        val diffCallback = BackupDiffUtilCallback(
            oldList = backupItemsList,
            newList = newItems,
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        backupItemsList.clear()
        backupItemsList.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomThemeRecyclerViewHolder {
        return adapterDelegatesManager.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(
        theme: CustomTheme,
        viewHolder: CustomThemeRecyclerViewHolder,
        position: Int,
    ) {
        adapterDelegatesManager.onBindViewHolder(backupItemsList[position], viewHolder, theme)
    }

    override fun getItemViewType(position: Int): Int {
        return adapterDelegatesManager.getItemViewType(backupItemsList[position])
    }

    override fun getItemCount() = backupItemsList.size

    class BackupDiffUtilCallback(
        private val oldList: List<BackupListItemModel>,
        private val newList: List<BackupListItemModel>,
    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].itemId == newList[newItemPosition].itemId

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            val isSameId = oldItem.itemId == newItem.itemId
            if (oldItem is BackupInfoListItemModel && newItem is BackupInfoListItemModel) {
                return isSameId && oldItem.areContentsTheSame(newItem)
            }
            if (oldItem is SimpleBackupActionListItemModel && newItem is SimpleBackupActionListItemModel) {
                return isSameId && oldItem.areContentsTheSame(newItem)
            }
            return isSameId
        }
    }

    companion object {
        const val BACKUP_INFO_TYPE = 1
        const val CLEAR_BACKUP_TYPE = BACKUP_INFO_TYPE + 1
        const val AUTO_BACKUP_TYPE = CLEAR_BACKUP_TYPE + 1
        const val NO_BACKUP_STORAGE_TYPE = AUTO_BACKUP_TYPE + 1
        const val BACKUP_STORAGE_TYPE = NO_BACKUP_STORAGE_TYPE + 1
    }
}