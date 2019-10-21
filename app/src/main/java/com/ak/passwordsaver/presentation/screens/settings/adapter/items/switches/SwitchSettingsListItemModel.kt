package com.ak.passwordsaver.presentation.screens.settings.adapter.items.switches

import com.ak.passwordsaver.presentation.screens.settings.adapter.items.SettingsListItemModel

class SwitchSettingsListItemModel(
    settingId: Int,
    settingName: String,
    val settingDescription: String,
    var isChecked: Boolean
) : SettingsListItemModel(settingId, settingName)