package com.ak.settings.adapter.items.spinners

import com.ak.settings.adapter.items.SettingsListItemModel

class SpinnerSettingsListItemModel(
    settingId: Int,
    settingName: String,
    val settingDescription: String,
    var selectedItemPosition: Int,
    val spinnerItems: List<String>
) : SettingsListItemModel(settingId, settingName)