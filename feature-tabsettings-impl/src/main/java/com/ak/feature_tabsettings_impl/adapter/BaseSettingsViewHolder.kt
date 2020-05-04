package com.ak.feature_tabsettings_impl.adapter

import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.ak.feature_tabsettings_impl.R
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel

abstract class BaseSettingsViewHolder<ItemModel : SettingsListItemModel>(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    fun bindViewHolder(itemModel: ItemModel) {
        itemView.findViewById<TextView>(R.id.tvSettingName).text = itemModel.settingName
        itemView.findViewById<TextView>(R.id.tvNewBadgeText).isVisible = itemModel.hasNewBadge
        setViewHolderData(itemModel)
    }

    abstract fun setViewHolderData(itemModel: ItemModel)
}