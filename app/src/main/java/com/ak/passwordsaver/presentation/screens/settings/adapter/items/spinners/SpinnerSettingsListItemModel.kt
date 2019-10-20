package com.ak.passwordsaver.presentation.screens.settings.adapter.items.spinners

import com.ak.passwordsaver.presentation.screens.settings.adapter.items.SettingsListItemModel

class SpinnerSettingsListItemModel(
    settingId: Int,
    settingName: String,
    val settingDescription: String,
    val selectedItemPosition: Int,
    val spinnerItems: List<String>
) : SettingsListItemModel(settingId, settingName)