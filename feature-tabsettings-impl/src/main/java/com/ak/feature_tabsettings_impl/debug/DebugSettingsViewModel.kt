package com.ak.feature_tabsettings_impl.debug

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ak.base.livedata.SingleEventLiveData
import com.ak.base.viewmodel.BasePSViewModel
import com.ak.core_repo_api.intefaces.IAccountsRepository
import com.ak.core_repo_api.intefaces.IPasswordsRepository
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.feature_appupdate_api.interfaces.IFeaturesUpdateManager
import com.ak.feature_tabsettings_impl.R
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel
import com.ak.feature_tabsettings_impl.adapter.items.switches.SwitchSettingsListItemModel
import com.ak.feature_tabsettings_impl.adapter.items.texts.TextSettingsListItemModel
import com.ak.feature_tabsettings_impl.design.DesignSettingsViewModel
import javax.inject.Inject

class DebugSettingsViewModel @Inject constructor(
    private val featuresUpdateManager: IFeaturesUpdateManager,
    private val resourceManager: IResourceManager,
    private val passwordsRepository: IPasswordsRepository,
    private val accountsRepository: IAccountsRepository,
) : BasePSViewModel() {

    private val debugSettingsListLiveData = MutableLiveData<List<SettingsListItemModel>>()
    private val switchToNextThemeLiveData = SingleEventLiveData<Boolean>()

    fun subscribeToDebugSettingsList(): LiveData<List<SettingsListItemModel>> = debugSettingsListLiveData
    fun subscribeToSwitchToNextTheme(): LiveData<Boolean> = switchToNextThemeLiveData

    fun onSwitchSettingsItemChanged(settingId: Int, isChecked: Boolean) {
        when (settingId) {
            SWITCH_THEME_PERIODICALLY_SETTING_ID -> {
                switchToNextThemeLiveData.value = isChecked
            }
            else -> {
                // no op
            }
        }
    }

    fun onSettingTextItemClicked(settingId: Int) {
        when (settingId) {
            RESET_ACCOUNTS_FEATURE_NEW_BADGE_SETTING_ID -> {
                featuresUpdateManager.resetTabAccountsFeatureViewedState()
            }
            RESET_FINGERPRINT_FEATURE_NEW_BADGE_SETTING_ID -> {
                featuresUpdateManager.resetFingerprintFeatureViewedState()
            }
            RESET_APP_THEME_FEATURE_NEW_BADGE_SETTING_ID -> {
                featuresUpdateManager.resetAppThemeFeatureViewedState()
            }
            RESET_PASSWORDS_STORAGE_SETTING_ID -> {
                passwordsRepository.clearAll().subscribe().let(this::bindDisposable)
            }
            RESET_ACCOUNTS_STORAGE_SETTING_ID -> {
                accountsRepository.clearAll().subscribe().let(this::bindDisposable)
            }
            else -> {
                // no op
            }
        }
    }

    fun loadDebugSettings() {
        val switchThemePeriodically = SwitchSettingsListItemModel(
            SWITCH_THEME_PERIODICALLY_SETTING_ID,
            resourceManager.getString(R.string.debug_periodic_theme_switch_setting_name),
            resourceManager.getString(
                R.string.debug_periodic_theme_switch_setting_desc,
                DebugNextThemeSwitcher.NEXT_THEME_SWITCH_PERIOD_IN_SECONDS,
            ),
            DebugNextThemeSwitcher.isThemeSwitchingEnabled(),
        )

        val resetAccountsFeatureNewBadge = TextSettingsListItemModel(
            RESET_ACCOUNTS_FEATURE_NEW_BADGE_SETTING_ID,
            resourceManager.getString(R.string.debug_reset_accounts_new_badge_setting_name),
        )

        val resetFingerprintFeatureNewBadge = TextSettingsListItemModel(
            RESET_FINGERPRINT_FEATURE_NEW_BADGE_SETTING_ID,
            resourceManager.getString(R.string.debug_reset_fingerprint_new_badge_setting_name),
        )

        val resetAppThemeFeatureNewBadge = TextSettingsListItemModel(
            RESET_APP_THEME_FEATURE_NEW_BADGE_SETTING_ID,
            resourceManager.getString(R.string.debug_reset_app_theme_new_badge_setting_name),
        )

        val resetPasswordsStorage = TextSettingsListItemModel(
            RESET_PASSWORDS_STORAGE_SETTING_ID,
            resourceManager.getString(R.string.debug_reset_passwords_storage_setting_name),
        )

        val resetAccountsStorage = TextSettingsListItemModel(
            RESET_ACCOUNTS_STORAGE_SETTING_ID,
            resourceManager.getString(R.string.debug_reset_accounts_storage_setting_name),
        )

        debugSettingsListLiveData.value = listOfNotNull(
            switchThemePeriodically,
            resetAccountsFeatureNewBadge,
            resetFingerprintFeatureNewBadge,
            resetAppThemeFeatureNewBadge,
            resetPasswordsStorage,
            resetAccountsStorage,
        )
    }

    private companion object {
        const val SWITCH_THEME_PERIODICALLY_SETTING_ID = 0
        const val RESET_ACCOUNTS_FEATURE_NEW_BADGE_SETTING_ID = 1
        const val RESET_FINGERPRINT_FEATURE_NEW_BADGE_SETTING_ID = 2
        const val RESET_APP_THEME_FEATURE_NEW_BADGE_SETTING_ID = 3
        const val RESET_PASSWORDS_STORAGE_SETTING_ID = 4
        const val RESET_ACCOUNTS_STORAGE_SETTING_ID = 5
    }
}