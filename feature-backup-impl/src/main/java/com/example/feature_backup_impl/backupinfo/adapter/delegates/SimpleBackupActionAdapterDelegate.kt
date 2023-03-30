package com.example.feature_backup_impl.backupinfo.adapter.delegates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.applier.CustomThemeApplier
import com.ak.app_theme.theme.uicomponents.recyclerview.CustomThemeRecyclerViewHolder
import com.ak.base.adapter.AdapterDelegate
import com.ak.feature_backup_impl.R
import com.example.feature_backup_impl.backupinfo.adapter.BackupListItemModel
import com.example.feature_backup_impl.backupinfo.adapter.SimpleBackupActionListItemModel

class SimpleBackupActionAdapterDelegate(
    private val viewType: Int,
    private val actionClickListener: (itemId: Int) -> Unit,
) : AdapterDelegate<BackupListItemModel> {

    override fun isForViewType(item: BackupListItemModel): Boolean {
        return item is SimpleBackupActionListItemModel
    }

    override fun getItemViewType() = viewType

    override fun onCreateViewHolder(parent: ViewGroup): CustomThemeRecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.simple_backup_action_item_layout, parent, false)
        return SimpleBackupActionViewHolder(itemView, actionClickListener)
    }

    override fun onBindViewHolder(
        item: BackupListItemModel,
        viewHolder: CustomThemeRecyclerViewHolder,
        theme: CustomTheme,
    ) {
        val itemModel = item as SimpleBackupActionListItemModel
        val holder = viewHolder as SimpleBackupActionViewHolder
        holder.bind(itemModel)
    }
}

class SimpleBackupActionViewHolder(
    itemView: View,
    private val actionClickListener: (itemId: Int) -> Unit,
) : CustomThemeRecyclerViewHolder(itemView) {

    private val backupActionTextView by lazy { itemView.findViewById<TextView>(R.id.tvSimpleBackupActionName) }

    override fun applyTheme(theme: CustomTheme) {
        CustomThemeApplier.applyTextColor(theme, backupActionTextView, R.attr.themedPrimaryTextColor)
    }

    fun bind(item: SimpleBackupActionListItemModel) {
        backupActionTextView.text = item.actionText
        itemView.setOnClickListener {
            actionClickListener(item.itemId)
        }
    }
}