package com.ak.feature_tabsettings_impl.adapter.items.texts

import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel

class TextSettingsListItemModel(
    settingId: Int,
    settingName: String,
    hasNewBadge: Boolean = false
) : SettingsListItemModel(settingId, settingName, hasNewBadge)