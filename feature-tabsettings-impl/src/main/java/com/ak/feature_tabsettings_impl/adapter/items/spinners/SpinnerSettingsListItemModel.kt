package com.ak.feature_tabsettings_impl.adapter.items.spinners

import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel

class SpinnerSettingsListItemModel(
    settingId: Int,
    settingName: String,
    val settingDescription: String,
    var selectedItemPosition: Int,
    val spinnerItems: List<String>,
    hasNewBadge: Boolean = false
) : SettingsListItemModel(settingId, settingName, hasNewBadge)