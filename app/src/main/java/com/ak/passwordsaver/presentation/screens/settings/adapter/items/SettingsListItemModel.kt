package com.ak.passwordsaver.presentation.screens.settings.adapter.items

abstract class SettingsListItemModel(
    val settingType: Int,
    val settingName: String,
    val settingDescription: String
) {
    companion object {
        const val SWITCH_SETTING_TYPE = 1
        const val SPINNER_SETTING_TYPE = 2
    }
}