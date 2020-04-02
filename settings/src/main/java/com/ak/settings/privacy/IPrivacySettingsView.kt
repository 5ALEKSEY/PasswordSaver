package com.ak.settings.privacy

import com.ak.base.ui.IBaseAppView
import com.ak.settings.adapter.items.SettingsListItemModel

interface IPrivacySettingsView : IBaseAppView {
    fun openAddPincodeScreen()
    fun openChangePincodeScreen()
    fun openAddPatternScreen()
    fun openChangePatternScreen()
    fun displayAppSettings(settingsItems: List<SettingsListItemModel>)
}