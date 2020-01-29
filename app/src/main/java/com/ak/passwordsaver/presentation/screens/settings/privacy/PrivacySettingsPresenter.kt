package com.ak.passwordsaver.presentation.screens.settings.privacy

import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.data.model.preferences.settings.ISettingsPreferencesManager
import com.ak.passwordsaver.presentation.base.BasePSPresenter
import com.ak.passwordsaver.presentation.base.managers.auth.AppLockStateHelper
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.SettingsListItemModel
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.spinners.SpinnerSettingsListItemModel
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.switches.SwitchSettingsListItemModel
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.texts.TextSettingsListItemModel
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class PrivacySettingsPresenter @Inject constructor(
    private val settingsPreferencesManager: ISettingsPreferencesManager
) : BasePSPresenter<IPrivacySettingsView>() {

    companion object {
        private const val PINCODE_ENABLE_SETTINGS_ID = 1
        private const val PINCODE_CHANGE_SETTINGS_ID = 2
        private const val LOCK_DELAY_CHANGE_SETTINGS_ID = 3
        private const val PATTERN_ENABLE_SETTINGS_ID = 4
        private const val PATTERN_CHANGE_SETTINGS_ID = 5
    }

    init {
        PSApplication.appInstance.getApplicationComponent().inject(this)
    }

    fun onSwitchSettingsItemChanged(settingId: Int, isChecked: Boolean) {
        when (settingId) {
            PINCODE_ENABLE_SETTINGS_ID -> {
                if (!isChecked) {
                    deleteUserPincodeData()
                    loadSettingsData()
                } else {
                    viewState.openAddPincodeScreen()
                }
            }
            PATTERN_ENABLE_SETTINGS_ID -> {
                if (!isChecked) {
                    deleteUserPatternCodeData()
                    loadSettingsData()
                } else {
                    viewState.openAddPatternScreen()
                }
            }
        }
    }

    fun onTextSettingsItemClicked(settingId: Int) {
        when (settingId) {
            PINCODE_CHANGE_SETTINGS_ID -> {
                viewState.openChangePincodeScreen()
            }
            PATTERN_CHANGE_SETTINGS_ID -> {
                viewState.openChangePatternScreen()
            }
        }
    }

    fun onSpinnerItemChanged(settingId: Int, newDataId: Int) {
        when (settingId) {
            LOCK_DELAY_CHANGE_SETTINGS_ID -> {
                settingsPreferencesManager.setLockAppStateChoose(
                    AppLockStateHelper.convertFromLockStateId(newDataId)
                )
            }
        }
    }

    fun loadSettingsData() {
        // Pincode
        val isPincodeEnabled = settingsPreferencesManager.isPincodeEnabled()
        val pincodeSwitchItemModel = SwitchSettingsListItemModel(
            PINCODE_ENABLE_SETTINGS_ID,
            "Pincode",
            "If you turn on this option, you can use privacy check before open your password content",
            isPincodeEnabled
        )
        val items = mutableListOf<SettingsListItemModel>(pincodeSwitchItemModel) // default list

        if (isPincodeEnabled) {
            //Change pincode
            val pincodeChangeTextItemModel = TextSettingsListItemModel(
                PINCODE_CHANGE_SETTINGS_ID,
                "Change pincode"
            )
            items.add(pincodeChangeTextItemModel)

            // Change lock delay
            val selectedDelayId = settingsPreferencesManager.getLockAppStateChoose().lockStateId
            val lockDelaysList = settingsPreferencesManager.getLockAppStatesList()
            val lockDelayChangeSpinnerItemModel = SpinnerSettingsListItemModel(
                LOCK_DELAY_CHANGE_SETTINGS_ID,
                "Lock delay",
                "Choose delay which you want to use for lock you application",
                selectedDelayId,
                lockDelaysList
            )
            items.add(lockDelayChangeSpinnerItemModel)

            // Pattern
            val isPatternEnabled = settingsPreferencesManager.isPatternEnabled()
            val patternSwitchItemModel = SwitchSettingsListItemModel(
                PATTERN_ENABLE_SETTINGS_ID,
                "Pattern",
                "You can use graphic pattern code for check your password content",
                isPatternEnabled
            )
            items.add(patternSwitchItemModel)

            // Change pattern
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

    private fun deleteUserPincodeData() {
        settingsPreferencesManager.setPincodeEnableState(false)
        settingsPreferencesManager.setUserPincodeValue("")
        deleteUserPatternCodeData()
    }

    private fun deleteUserPatternCodeData() {
        settingsPreferencesManager.setPatternEnableState(false)
        settingsPreferencesManager.setUserPatternValue("")
    }
}