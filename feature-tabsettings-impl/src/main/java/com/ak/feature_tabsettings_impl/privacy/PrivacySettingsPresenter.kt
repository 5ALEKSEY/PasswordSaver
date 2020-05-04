package com.ak.feature_tabsettings_impl.privacy

import com.ak.base.presenter.BasePSPresenter
import com.ak.core_repo_api.intefaces.AppLockStateHelper
import com.ak.core_repo_api.intefaces.ISettingsPreferencesManager
import com.ak.feature_appupdate_api.interfaces.IFeaturesUpdateManager
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
    private val psBiometricManager: IPSBiometricManager,
    private val featuresUpdateManager: IFeaturesUpdateManager
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
                when(psBiometricManager.getBiometricFeatureAvailableStatus()) {
                    IPSBiometricManager.AvailableStatus.AVAILABLE -> {
                        settingsPreferencesManager.setBiometricEnableState(isChecked)
                        loadSettingsData()
                    }
                    IPSBiometricManager.AvailableStatus.NO_SAVED_FINGERPRINTS -> {
                        if (isChecked) {
                            viewState.showAddNewFingerprintDialog()
                        }
                    }
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
            var biometricSwitchItemModel: SwitchSettingsListItemModel? = SwitchSettingsListItemModel(
                    BIOMETRIC_ENABLE_SETTINGS_ID,
                    "Fingerprint",
                    "You can use fingerprint for fast unlock your app",
                    false,
                    isBiometricSettingsModelHasNewBadge()
            )
            when (psBiometricManager.getBiometricFeatureAvailableStatus()) {
                IPSBiometricManager.AvailableStatus.AVAILABLE -> {
                    biometricSwitchItemModel?.isChecked = isBiometricEnabled
                }
                IPSBiometricManager.AvailableStatus.NO_SAVED_FINGERPRINTS -> {
                    // user will route to phone security settings to add new fingerprint
                    settingsPreferencesManager.setBiometricEnableState(false)
                }
                else -> {
                    biometricSwitchItemModel = null
                    settingsPreferencesManager.setBiometricEnableState(false)
                }
            }
            biometricSwitchItemModel?.let {
                featuresUpdateManager.markFingerprintFeatureAsViewed()
                settingsItems.add(it)
            }

        }

        viewState.displayAppSettings(settingsItems)
    }

    private fun isBiometricSettingsModelHasNewBadge() = !featuresUpdateManager.isFingerprintFeatureViewed()

    private fun deleteUserPincodeData() {
        settingsPreferencesManager.setPincodeEnableState(false)
        settingsPreferencesManager.setUserPincodeValue("")

        // disable another privacy features
        settingsPreferencesManager.setBiometricEnableState(false)
        deleteUserPatternCodeData()
    }

    private fun deleteUserPatternCodeData() {
        settingsPreferencesManager.setPatternEnableState(false)
        settingsPreferencesManager.setUserPatternValue("")
    }
}