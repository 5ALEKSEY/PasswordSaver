package com.ak.passwordsaver.presentation.screens.settings.adapter.items.texts

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.base.adapter.AdapterDelegate
import com.ak.passwordsaver.presentation.screens.settings.adapter.BaseSettingsViewHolder
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.SettingsListItemModel
import com.ak.passwordsaver.utils.extensions.setSafeClickListener

class TextAdapterDelegate(
    private val viewType: Int,
    private val onTextSettingsClicked: (settingId: Int) -> Unit
) : AdapterDelegate<SettingsListItemModel> {

    override fun isForViewType(item: SettingsListItemModel) = item is TextSettingsListItemModel

    override fun getItemViewType() = viewType

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.settings_item_text_layout, parent, false)
        return SettingsTextHolder(itemView, onTextSettingsClicked)
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