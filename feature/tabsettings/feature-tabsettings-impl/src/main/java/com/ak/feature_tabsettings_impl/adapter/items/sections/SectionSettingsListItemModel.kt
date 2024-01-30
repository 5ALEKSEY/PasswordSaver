package com.ak.feature_tabsettings_impl.adapter.items.sections

import androidx.annotation.DrawableRes
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel

class SectionSettingsListItemModel(
    settingId: Int,
    settingName: String,
    @DrawableRes
    val imageRes: Int,
    hasNewBadge: Boolean = false
) : SettingsListItemModel(settingId, settingName, hasNewBadge)