package com.ak.passwordsaver.presentation.screens.settings.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.SettingsListItemModel
import com.ak.passwordsaver.utils.bindView

abstract class BaseSettingsViewHolder<ItemModel : SettingsListItemModel>(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    private val mName: TextView by bindView(R.id.tv_setting_name)
    private val mDescription: TextView by bindView(R.id.tv_setting_description)

    fun bindViewHolder(itemModel: ItemModel) {
        mName.text = itemModel.settingName
        mDescription.text = itemModel.settingDescription
        setViewHolderData(itemModel)
    }

    abstract fun setViewHolderData(itemModel: ItemModel)
}