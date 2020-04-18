package com.ak.feature_tabsettings_impl.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ak.feature_tabsettings_impl.R
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel

abstract class BaseSettingsViewHolder<ItemModel : SettingsListItemModel>(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    fun bindViewHolder(itemModel: ItemModel) {
        itemView.findViewById<TextView>(R.id.tvSettingName).text = itemModel.settingName
        setViewHolderData(itemModel)
    }

    abstract fun setViewHolderData(itemModel: ItemModel)
}