package com.ak.settings.adapter.items.sections

import androidx.annotation.DrawableRes
import com.ak.settings.adapter.items.SettingsListItemModel

class SectionSettingsListItemModel(
    settingId: Int,
    settingName: String,
    @DrawableRes
    val imageRes: Int
) : SettingsListItemModel(settingId, settingName)