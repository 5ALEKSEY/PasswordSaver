package com.ak.settings.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ak.settings.R
import com.ak.settings.adapter.items.SettingsListItemModel

abstract class BaseSettingsViewHolder<ItemModel : SettingsListItemModel>(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    fun bindViewHolder(itemModel: ItemModel) {
        itemView.findViewById<TextView>(R.id.tvSettingName).text = itemModel.settingName
        setViewHolderData(itemModel)
    }

    abstract fun setViewHolderData(itemModel: ItemModel)
}