package com.ak.passwordsaver.presentation.screens.settings.adapter.items.switches

import com.ak.passwordsaver.presentation.screens.settings.adapter.items.SettingsListItemModel

class SwitchSettingsListItemModel(
    settingName: String,
    settingDescription: String,
    val isChecked: Boolean
) : SettingsListItemModel(SettingsListItemModel.SWITCH_SETTING_TYPE, settingName, settingDescription)