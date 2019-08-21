package com.ak.passwordsaver.presentation.screens.settings.adapter.items.switches

import android.view.View
import android.widget.Switch
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.screens.settings.adapter.BaseSettingsViewHolder
import com.ak.passwordsaver.utils.bindView

class SettingsSwitchHolder(itemView: View) : BaseSettingsViewHolder<SwitchSettingsListItemModel>(itemView) {

    private val mSwitch: Switch by bindView(R.id.s_setting_enabling_state)

    override fun setViewHolderData(itemModel: SwitchSettingsListItemModel) {
        mSwitch.isChecked = itemModel.isChecked
    }
}