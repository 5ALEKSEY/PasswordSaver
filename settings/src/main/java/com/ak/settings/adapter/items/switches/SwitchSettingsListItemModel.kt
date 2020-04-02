package com.ak.settings.adapter.items.switches

import com.ak.settings.adapter.items.SettingsListItemModel

class SwitchSettingsListItemModel(
    settingId: Int,
    settingName: String,
    val settingDescription: String,
    var isChecked: Boolean
) : SettingsListItemModel(settingId, settingName)