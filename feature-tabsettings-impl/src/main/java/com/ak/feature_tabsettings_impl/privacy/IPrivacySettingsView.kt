package com.ak.feature_tabsettings_impl.privacy

import com.ak.base.ui.IBaseAppView
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel

interface IPrivacySettingsView : IBaseAppView {
    fun openAddPincodeScreen()
    fun openChangePincodeScreen()
    fun openAddPatternScreen()
    fun openChangePatternScreen()
    fun showAddNewFingerprintDialog()
    fun displayAppSettings(settingsItems: List<SettingsListItemModel>)
}