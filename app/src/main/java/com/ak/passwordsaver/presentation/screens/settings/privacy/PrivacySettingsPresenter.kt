package com.ak.passwordsaver.presentation.screens.settings.privacy

import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.model.preferences.SettingsPreferencesManager
import com.ak.passwordsaver.presentation.base.BasePSPresenter
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.SettingsListItemModel
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.switches.SwitchSettingsListItemModel
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.texts.TextSettingsListItemModel
import com.arellomobile.mvp.InjectViewState
import javax.inject.Inject

@InjectViewState
class PrivacySettingsPresenter : BasePSPresenter<IPrivacySettingsView>() {

    companion object {
        private const val PINCODE_ENABLE_SETTINGS_ID = 1
        private const val PINCODE_CHANGE_SETTINGS_ID = 2
        private const val PATTERN_ENABLE_SETTINGS_ID = 4
        private const val PATTERN_CHANGE_SETTINGS_ID = 5
    }

    @Inject
    lateinit var mSettingsPreferencesManager: SettingsPreferencesManager

    init {
        PSApplication.appInstance.getApplicationComponent().inject(this)
    }

    fun onSwitchSettingsItemChanged(settingId: Int, isChecked: Boolean) {
        when (settingId) {
            PINCODE_ENABLE_SETTINGS_ID -> {
                mSettingsPreferencesManager.setPincodeEnableState(isChecked)
                loadSettingsData()
            }
            PATTERN_ENABLE_SETTINGS_ID -> {
                mSettingsPreferencesManager.setPatternEnableState(isChecked)
                loadSettingsData()
            }
        }
    }

    fun onTextSettingsItemClicked(settingId: Int) {
        when (settingId) {
            PINCODE_CHANGE_SETTINGS_ID -> {
                viewState.showShortTimeMessage("Change pincode")
            }
            PATTERN_CHANGE_SETTINGS_ID -> {
                viewState.showShortTimeMessage("Change pattern")
            }
        }
    }

    fun loadSettingsData() {
        // Pincode
        val isPincodeEnabled = mSettingsPreferencesManager.isPincodeEnabled()
        val pincodeSwitchItemModel = SwitchSettingsListItemModel(
            PINCODE_ENABLE_SETTINGS_ID,
            "Pincode",
            "If you turn on this option, you can use privacy check before open your password content",
            isPincodeEnabled
        )
        val items = mutableListOf<SettingsListItemModel>(pincodeSwitchItemModel) // default list

        if (isPincodeEnabled) {
            val pincodeChangeTextItemModel = TextSettingsListItemModel(
                PINCODE_CHANGE_SETTINGS_ID,
                "Change pincode"
            )
            items.add(pincodeChangeTextItemModel)

            // Pattern
            val isPatternEnabled = mSettingsPreferencesManager.isPatternEnabled()
            val patternSwitchItemModel = SwitchSettingsListItemModel(
                PATTERN_ENABLE_SETTINGS_ID,
                "Pattern",
                "You can use graphic pattern code for check your password content",
                isPatternEnabled
            )
            items.add(patternSwitchItemModel)

            if (isPatternEnabled) {
                val patternChangeTextItemModel = TextSettingsListItemModel(
                    PATTERN_CHANGE_SETTINGS_ID,
                    "Change your pattern"
                )
                items.add(patternChangeTextItemModel)
            }
        }

        viewState.displayAppSettings(items)
    }
}