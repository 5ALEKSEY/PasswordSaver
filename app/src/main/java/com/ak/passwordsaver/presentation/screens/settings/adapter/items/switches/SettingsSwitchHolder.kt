package com.ak.passwordsaver.presentation.screens.settings.adapter.items.switches

import android.view.View
import android.widget.Switch
import android.widget.TextView
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.screens.settings.adapter.BaseSettingsViewHolder
import com.ak.passwordsaver.utils.bindView

class SettingsSwitchHolder(itemView: View) : BaseSettingsViewHolder<SwitchSettingsListItemModel>(itemView) {

    private val mName: TextView by bindView(R.id.tv_setting_name)
    private val mDescription: TextView by bindView(R.id.tv_setting_description)
    private val mSwitch: Switch by bindView(R.id.s_setting_enabling_state)

    override fun bindViewHolder(itemModel: SwitchSettingsListItemModel) {
        mName.text = itemModel.settingName
        mDescription.text = itemModel.settingDescription
        mSwitch.isChecked = itemModel.isChecked
    }
}