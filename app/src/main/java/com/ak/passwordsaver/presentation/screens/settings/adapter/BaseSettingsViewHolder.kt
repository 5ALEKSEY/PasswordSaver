package com.ak.passwordsaver.presentation.screens.settings.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.SettingsListItemModel

abstract class BaseSettingsViewHolder<ItemModel : SettingsListItemModel>(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    fun bindViewHolder(itemModel: ItemModel) {
        itemView.findViewById<TextView>(R.id.tvSettingName).text = itemModel.settingName
        setViewHolderData(itemModel)
    }

    abstract fun setViewHolderData(itemModel: ItemModel)
}