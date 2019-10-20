package com.ak.passwordsaver.presentation.screens.settings.adapter.items.sections

import android.support.annotation.DrawableRes
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.SettingsListItemModel

class SectionSettingsListItemModel(
    settingId: Int,
    settingName: String,
    @DrawableRes
    val imageRes: Int
) : SettingsListItemModel(settingId, settingName)