package com.ak.feature_tabsettings_impl.adapter.items.themechange

import com.ak.app_theme.theme.CustomTheme
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel

class ThemeChangeSettingsListItemModel(
    settingId: Int,
    settingName: String,
    val themes: List<CustomTheme.Description>,
    val selectedTheme: CustomTheme.Description,
    hasNewBadge: Boolean = false,
) : SettingsListItemModel(settingId, settingName, hasNewBadge)
