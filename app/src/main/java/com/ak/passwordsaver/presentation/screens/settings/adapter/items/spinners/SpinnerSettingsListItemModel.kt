package com.ak.passwordsaver.presentation.screens.settings.adapter.items.spinners

import com.ak.passwordsaver.presentation.screens.settings.adapter.items.SettingsListItemModel

class SpinnerSettingsListItemModel(
    settingName: String,
    settingDescription: String,
    val selectedItemPosition: Int,
    val spinnerItems: List<String>
) : SettingsListItemModel(SettingsListItemModel.SPINNER_SETTING_TYPE, settingName, settingDescription)