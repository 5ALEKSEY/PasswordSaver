package com.ak.feature_tabsettings_impl.privacy

import com.ak.base.presenter.BasePSPresenter
import com.ak.core_repo_api.intefaces.AppLockStateHelper
import com.ak.core_repo_api.intefaces.ISettingsPreferencesManager
import com.ak.feature_security_api.interfaces.IPSBiometricManager
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel
import com.ak.feature_tabsettings_impl.adapter.items.spinners.SpinnerSettingsListItemModel
import com.ak.feature_tabsettings_impl.adapter.items.switches.SwitchSettingsListItemModel
import com.ak.feature_tabsettings_impl.adapter.items.texts.TextSettingsListItemModel
import com.ak.feature_tabsettings_impl.di.FeatureTabSettingsComponent
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class PrivacySettingsPresenter @Inject constructor(
    private val settingsPreferencesManager: ISettingsPreferencesManager,
    private val psBiometricManager: IPSBiometricManager
) : BasePSPresenter<IPrivacySettingsView>() {

    companion object {
        private const val PINCODE_ENABLE_SETTINGS_ID = 1
        private const val PINCODE_CHANGE_SETTINGS_ID = 2
        private const val LOCK_DELAY_CHANGE_SETTINGS_ID = 3
        private const val PATTERN_ENABLE_SETTINGS_ID = 4
        private const val PATTERN_CHANGE_SETTINGS_ID = 5
        private const val BIOMETRIC_ENABLE_SETTINGS_ID = 6
    }

    init {
        FeatureTabSettingsComponent.get().inject(this)
    }

    fun onSwitchSettingsItemChanged(settingId: Int, isChecked: Boolean) {
        when (settingId) {
            PINCODE_ENABLE_SETTINGS_ID -> {
                if (isChecked) {
                    viewState.openAddPincodeScreen()
                } else {
                    deleteUserPincodeData()
                    loadSettingsData()
                }
            }
            PATTERN_ENABLE_SETTINGS_ID -> {
                if (isChecked) {
                    viewState.openAddPatternScreen()
                } else {
                    deleteUserPatternCodeData()
                    loadSettingsData()
                }
            }
            BIOMETRIC_ENABLE_SETTINGS_ID -> {
                settingsPreferencesManager.setBiometricEnableState(isChecked)
                loadSettingsData()
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
                "If you turn on this option, you can use privacy check before open your application",
                isPincodeEnabled
        )
        val settingsItems = mutableListOf<SettingsListItemModel>(pincodeSwitchItemModel) // default list

        if (isPincodeEnabled) {
            //Change pincode
            val pincodeChangeTextItemModel = TextSettingsListItemModel(
                    PINCODE_CHANGE_SETTINGS_ID,
                    "Change pincode"
            )
            settingsItems.add(pincodeChangeTextItemModel)

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
            settingsItems.add(lockDelayChangeSpinnerItemModel)

            // Pattern
            val isPatternEnabled = settingsPreferencesManager.isPatternEnabled()
            val patternSwitchItemModel = SwitchSettingsListItemModel(
                    PATTERN_ENABLE_SETTINGS_ID,
                    "Pattern",
                    "You can use graphic pattern code for unlock your app",
                    isPatternEnabled
            )
            settingsItems.add(patternSwitchItemModel)

            // Change pattern
            if (isPatternEnabled) {
                val patternChangeTextItemModel = TextSettingsListItemModel(
                        PATTERN_CHANGE_SETTINGS_ID,
                        "Change your pattern"
                )
                settingsItems.add(patternChangeTextItemModel)
            }

            // Fingerprint
            val isBiometricEnabled = settingsPreferencesManager.isBiometricEnabled()
            val biometricState = psBiometricManager.getBiometricFeatureAvailableStatus()
            var biometricSwitchItemModel: SwitchSettingsListItemModel? = null
            when (biometricState) {
                IPSBiometricManager.AvailableStatus.AVAILABLE -> {
                    biometricSwitchItemModel = SwitchSettingsListItemModel(
                            BIOMETRIC_ENABLE_SETTINGS_ID,
                            "Fingerprint",
                            "You can use fingerprint for fast unlock your app",
                            isBiometricEnabled
                    )
                }
                IPSBiometricManager.AvailableStatus.NO_SAVED_FINGERPRINTS -> {
                    // TODO: make message for user and open security settings in phone
                }
                else -> biometricSwitchItemModel = null
            }
            biometricSwitchItemModel?.let { settingsItems.add(it) }

        }

        viewState.displayAppSettings(settingsItems)
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