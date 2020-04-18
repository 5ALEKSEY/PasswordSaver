package com.ak.feature_tabsettings_impl.adapter.items.texts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ak.base.adapter.AdapterDelegate
import com.ak.base.extensions.setSafeClickListener
import com.ak.feature_tabsettings_impl.R
import com.ak.feature_tabsettings_impl.adapter.BaseSettingsViewHolder
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel

class TextAdapterDelegate(
    private val viewType: Int,
    private val onTextSettingsClicked: (settingId: Int) -> Unit
) : AdapterDelegate<SettingsListItemModel> {

    override fun isForViewType(item: SettingsListItemModel) = item is TextSettingsListItemModel

    override fun getItemViewType() = viewType

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.settings_item_text_layout, parent, false)
        return SettingsTextHolder(
            itemView,
            onTextSettingsClicked
        )
    }

    override fun onBindViewHolder(
        item: SettingsListItemModel,
        viewHolder: RecyclerView.ViewHolder
    ) {
        val itemModel = item as TextSettingsListItemModel
        val holder = viewHolder as SettingsTextHolder
        holder.bindViewHolder(itemModel)
    }
}

class SettingsTextHolder(
    itemView: View,
    private val onTextSettingsClicked: (settingId: Int) -> Unit
) : BaseSettingsViewHolder<TextSettingsListItemModel>(itemView) {

    override fun setViewHolderData(itemModel: TextSettingsListItemModel) {
        itemView.setSafeClickListener {
            onTextSettingsClicked(itemModel.settingId)
        }
    }
}