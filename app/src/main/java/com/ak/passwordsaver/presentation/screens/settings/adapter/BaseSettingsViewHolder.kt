package com.ak.passwordsaver.presentation.screens.settings.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.SettingsListItemModel

abstract class BaseSettingsViewHolder<ItemModel : SettingsListItemModel>(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    abstract fun bindViewHolder(itemModel: ItemModel)
}