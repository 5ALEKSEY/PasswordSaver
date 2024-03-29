package com.ak.feature_tabsettings_impl.privacy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ak.base.livedata.SingleEventLiveData
import com.ak.base.viewmodel.BasePSViewModel
import com.ak.core_repo_api.intefaces.AppLockStateHelper
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.core_repo_api.intefaces.preference.ISettingsPreferencesManager
import com.ak.feature_appupdate_api.interfaces.IFeaturesUpdateManager
import com.ak.feature_security_api.interfaces.IPSBiometricManager
import com.ak.feature_tabsettings_impl.R
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel
import com.ak.feature_tabsettings_impl.adapter.items.spinners.SpinnerSettingsListItemModel
import com.ak.feature_tabsettings_impl.adapter.items.switches.SwitchSettingsListItemModel
import com.ak.feature_tabsettings_impl.adapter.items.texts.TextSettingsListItemModel
import javax.inject.Inject

class PrivacySettingsViewModel @Inject constructor(
    private val settingsPreferencesManager: ISettingsPreferencesManager,
    private val psBiometricManager: IPSBiometricManager,
    private val featuresUpdateManager: IFeaturesUpdateManager,
    private val resourceManager: IResourceManager,
) : BasePSViewModel() {

    companion object {
        private const val PINCODE_ENABLE_SETTINGS_ID = 1
        private const val PINCODE_CHANGE_SETTINGS_ID = 2
        private const val LOCK_DELAY_CHANGE_SETTINGS_ID = 3
        private const val PATTERN_ENABLE_SETTINGS_ID = 4
        private const val PATTERN_CHANGE_SETTINGS_ID = 5
        private const val BIOMETRIC_ENABLE_SETTINGS_ID = 6
    }

    private val privacySettingsListLiveData = MutableLiveData<List<SettingsListItemModel>>()
    private val showAddPincodeLiveData = SingleEventLiveData<Unit?>()
    private val showEditPincodeLiveData = SingleEventLiveData<Unit?>()
    private val showAddPatternLiveData = SingleEventLiveData<Unit?>()
    private val showEditPatternLiveData = SingleEventLiveData<Unit?>()
    private val showAddNewFingerprintLiveData = SingleEventLiveData<Unit?>()

    fun subscribeToPrivacySettingsListLiveData(): LiveData<List<SettingsListItemModel>> = privacySettingsListLiveData
    fun subscribeToShowAddPincodeLiveData(): LiveData<Unit?> = showAddPincodeLiveData
    fun subscribeToShowEditPincodeLiveData(): LiveData<Unit?> = showEditPincodeLiveData
    fun subscribeToShowAddPatternLiveData(): LiveData<Unit?> = showAddPatternLiveData
    fun subscribeToShowEditPatternLiveData(): LiveData<Unit?> = showEditPatternLiveData
    fun subscribeToShowAddNewFingerprintLiveData(): LiveData<Unit?> = showAddNewFingerprintLiveData

    fun onSwitchSettingsItemChanged(settingId: Int, isChecked: Boolean) {
        when (settingId) {
            PINCODE_ENABLE_SETTINGS_ID -> {
                if (isChecked) {
                    showAddPincodeLiveData.call()
                } else {
                    deleteUserPincodeData()
                    loadSettingsData()
                }
            }
            PATTERN_ENABLE_SETTINGS_ID -> {
                if (isChecked) {
                    showAddPatternLiveData.call()
                } else {
                    deleteUserPatternCodeData()
                    loadSettingsData()
                }
            }
            BIOMETRIC_ENABLE_SETTINGS_ID -> {
                when (psBiometricManager.getBiometricFeatureAvailableStatus()) {
                    IPSBiometricManager.AvailableStatus.AVAILABLE -> {
                        settingsPreferencesManager.setBiometricEnableState(isChecked)
                        loadSettingsData()
                    }
                    IPSBiometricManager.AvailableStatus.NO_SAVED_FINGERPRINTS -> {
                        if (isChecked) {
                            showAddNewFingerprintLiveData.call()
                        }
                    }
                    IPSBiometricManager.AvailableStatus.UNAVAILABLE -> {
                        // no op
                    }
                }
            }
        }
    }

    fun onTextSettingsItemClicked(settingId: Int) {
        when (settingId) {
            PINCODE_CHANGE_SETTINGS_ID -> {
                showEditPincodeLiveData.call()
            }
            PATTERN_CHANGE_SETTINGS_ID -> {
                showEditPatternLiveData.call()
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
            resourceManager.getString(R.string.pincode_settings_name),
            resourceManager.getString(R.string.pincode_settings_desc),
            isPincodeEnabled
        )
        val settingsItems = mutableListOf<SettingsListItemModel>(pincodeSwitchItemModel) // default list

        if (isPincodeEnabled) {
            //Change pincode
            val pincodeChangeTextItemModel = TextSettingsListItemModel(
                PINCODE_CHANGE_SETTINGS_ID,
                resourceManager.getString(R.string.change_pincode_settings_name)
            )
            settingsItems.add(pincodeChangeTextItemModel)

            // Change lock delay
            val selectedDelayId = settingsPreferencesManager.getLockAppStateChoose().lockStateId
            val lockDelaysList = settingsPreferencesManager.getLockAppStatesList()
            val lockDelayChangeSpinnerItemModel = SpinnerSettingsListItemModel(
                LOCK_DELAY_CHANGE_SETTINGS_ID,
                resourceManager.getString(R.string.lock_delay_settings_name),
                resourceManager.getString(R.string.lock_delay_settings_desc),
                selectedDelayId,
                lockDelaysList
            )
            settingsItems.add(lockDelayChangeSpinnerItemModel)

            // Pattern
            val isPatternEnabled = settingsPreferencesManager.isPatternEnabled()
            val patternSwitchItemModel = SwitchSettingsListItemModel(
                PATTERN_ENABLE_SETTINGS_ID,
                resourceManager.getString(R.string.pattern_settings_name),
                resourceManager.getString(R.string.pattern_settings_desc),
                isPatternEnabled
            )
            settingsItems.add(patternSwitchItemModel)

            // Change pattern
            if (isPatternEnabled) {
                val patternChangeTextItemModel = TextSettingsListItemModel(
                    PATTERN_CHANGE_SETTINGS_ID,
                    resourceManager.getString(R.string.change_pattern_settings_name)
                )
                settingsItems.add(patternChangeTextItemModel)
            }

            // Fingerprint
            val isBiometricEnabled = settingsPreferencesManager.isBiometricEnabled()
            var biometricSwitchItemModel: SwitchSettingsListItemModel? = SwitchSettingsListItemModel(
                BIOMETRIC_ENABLE_SETTINGS_ID,
                resourceManager.getString(R.string.fingerprint_settings_name),
                resourceManager.getString(R.string.fingerprint_settings_desc),
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

        privacySettingsListLiveData.value = settingsItems
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