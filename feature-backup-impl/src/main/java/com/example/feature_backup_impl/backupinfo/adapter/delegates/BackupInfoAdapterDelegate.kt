package com.example.feature_backup_impl.backupinfo.adapter.delegates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.CustomThemeDrawableBuilder
import com.ak.app_theme.theme.applier.CustomThemeApplier
import com.ak.app_theme.theme.uicomponents.recyclerview.CustomThemeRecyclerViewHolder
import com.ak.base.adapter.AdapterDelegate
import com.ak.feature_backup_impl.R
import com.example.feature_backup_impl.backupinfo.adapter.BackupInfoListItemModel
import com.example.feature_backup_impl.backupinfo.adapter.BackupListItemModel

class BackupInfoAdapterDelegate(
    private val viewType: Int,
    private val shareBackupListener: () -> Unit,
    private val refreshBackupListener: () -> Unit,
): AdapterDelegate<BackupListItemModel> {

    override fun isForViewType(item: BackupListItemModel) = item is BackupInfoListItemModel

    override fun getItemViewType() = viewType

    override fun onCreateViewHolder(parent: ViewGroup): CustomThemeRecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.backup_info_item_layout, parent, false)
        return BackupInfoViewHolder(itemView, shareBackupListener, refreshBackupListener)
    }

    override fun onBindViewHolder(
        item: BackupListItemModel,
        viewHolder: CustomThemeRecyclerViewHolder,
        theme: CustomTheme,
    ) {
        val itemModel = item as BackupInfoListItemModel
        val holder = viewHolder as BackupInfoViewHolder
        holder.bind(itemModel)
    }
}

class BackupInfoViewHolder(
    itemView: View,
    private val shareBackupListener: () -> Unit,
    private val refreshBackupListener: () -> Unit,
) : CustomThemeRecyclerViewHolder(itemView) {

    private val backInfoItemNameTextView by lazy { itemView.findViewById<TextView>(R.id.tvBackInfoItemName) }
    private val backInfoItemDescTextView by lazy { itemView.findViewById<TextView>(R.id.tvBackInfoItemDesc) }
    private val refreshButton by lazy { itemView.findViewById<View>(R.id.civRefreshDumpBtn) }
    private val shareButton by lazy { itemView.findViewById<View>(R.id.civShareDumpBtn) }

    override fun applyTheme(theme: CustomTheme) {
        CustomThemeApplier.applyTextColor(theme, backInfoItemNameTextView, R.attr.themedPrimaryTextColor)
        CustomThemeApplier.applyTextColor(theme, backInfoItemDescTextView, R.attr.themedSecondaryTextColor)

        val actionButtonsBg = CustomThemeDrawableBuilder(theme, itemView.context)
            .oval()
            .solidColorAttr(R.attr.themedAccentColor)
            .radius(itemView.resources.getDimensionPixelSize(R.dimen.backup_info_buttons_size).toFloat())
            .build()
        refreshButton.background = actionButtonsBg
        shareButton.background = actionButtonsBg
    }

    fun bind(item: BackupInfoListItemModel) {
        backInfoItemDescTextView.text = itemView.context.getString(
            R.string.backup_info_text,
            item.passwordsCount,
            item.accountsCount,
            item.lastUpdateTime,
            item.backupFileSize,
        )
        refreshButton.setOnClickListener {
            refreshBackupListener.invoke()
        }
        shareButton.setOnClickListener {
            shareBackupListener.invoke()
        }
    }
}