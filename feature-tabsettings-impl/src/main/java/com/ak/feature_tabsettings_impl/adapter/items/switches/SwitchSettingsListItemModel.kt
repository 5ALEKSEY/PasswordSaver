package com.ak.feature_tabsettings_impl.adapter.items.switches

import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel

class SwitchSettingsListItemModel(
    settingId: Int,
    settingName: String,
    val settingDescription: String,
    var isChecked: Boolean
) : SettingsListItemModel(settingId, settingName)