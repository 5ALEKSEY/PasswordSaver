package com.ak.passwordsaver.presentation.screens.settings.adapter.items.switches

import com.ak.passwordsaver.presentation.screens.settings.adapter.items.SettingsListItemModel

class SwitchSettingsListItemModel(
    settingId: Int,
    settingName: String,
    settingDescription: String,
    val isChecked: Boolean
) : SettingsListItemModel(settingId, settingName, settingDescription)