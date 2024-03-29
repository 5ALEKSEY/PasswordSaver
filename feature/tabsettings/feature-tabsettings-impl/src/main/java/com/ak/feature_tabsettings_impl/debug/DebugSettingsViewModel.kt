package com.ak.feature_tabsettings_impl.debug

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ak.base.livedata.SingleEventLiveData
import com.ak.base.viewmodel.BasePSViewModel
import com.ak.core_repo_api.intefaces.account.AccountRepoEntity
import com.ak.core_repo_api.intefaces.account.IAccountsRepository
import com.ak.core_repo_api.intefaces.password.IPasswordsRepository
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.core_repo_api.intefaces.password.PasswordRepoEntity
import com.ak.core_repo_api.intefaces.theme.ICustomUserThemesRepository
import com.ak.feature_appupdate_api.interfaces.IFeaturesUpdateManager
import com.ak.feature_tabsettings_impl.R
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel
import com.ak.feature_tabsettings_impl.adapter.items.switches.SwitchSettingsListItemModel
import com.ak.feature_tabsettings_impl.adapter.items.texts.TextSettingsListItemModel
import javax.inject.Inject
import kotlin.random.Random
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DebugSettingsViewModel @Inject constructor(
    private val featuresUpdateManager: IFeaturesUpdateManager,
    private val resourceManager: IResourceManager,
    private val passwordsRepository: IPasswordsRepository,
    private val accountsRepository: IAccountsRepository,
    private val customThemesRepository: ICustomUserThemesRepository,
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
            RESET_BACKUP_FEATURE_NEW_BADGE_SETTING_ID -> {
                featuresUpdateManager.resetBackupFeatureViewedState()
            }
            RESET_PASSWORDS_STORAGE_SETTING_ID -> {
                viewModelScope.launch {
                    passwordsRepository.clearAll()
                    loadDebugSettings()
                }
            }
            RESET_ACCOUNTS_STORAGE_SETTING_ID -> {
                viewModelScope.launch {
                    accountsRepository.clearAll()
                    loadDebugSettings()
                }
            }
            RESET_CUSTOM_THEMES_STORAGE -> {
                viewModelScope.launch {
                    customThemesRepository.clearAll()
                    loadDebugSettings()
                }
            }
            ADD_RANDOM_PASSWORD_SETTING_ID -> {
                viewModelScope.launch {
                    passwordsRepository.addNewPasswords(listOf(generateRandomPassword()))
                    loadDebugSettings()
                }
            }
            ADD_RANDOM_ACCOUNT_SETTING_ID -> {
                viewModelScope.launch {
                    accountsRepository.addNewAccounts(listOf(generateRandomAccount()))
                    loadDebugSettings()
                }
            }
            else -> {
                // no op
            }
        }
    }

    fun loadDebugSettings() {
        viewModelScope.launch {
            debugSettingsListLiveData.value = getDebugSettingsList()
        }
    }

    private suspend fun getDebugSettingsList() = withContext(Dispatchers.IO) {
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

        val resetBackupFeatureNewBadge = TextSettingsListItemModel(
            RESET_BACKUP_FEATURE_NEW_BADGE_SETTING_ID,
            resourceManager.getString(R.string.debug_reset_backup_new_badge_setting_name),
        )

        val resetPasswordsStorage = TextSettingsListItemModel(
            RESET_PASSWORDS_STORAGE_SETTING_ID,
            resourceManager.getString(R.string.debug_reset_passwords_storage_setting_name),
        )

        val resetAccountsStorage = TextSettingsListItemModel(
            RESET_ACCOUNTS_STORAGE_SETTING_ID,
            resourceManager.getString(R.string.debug_reset_accounts_storage_setting_name),
        )

        val resetCustomThemesStorage = TextSettingsListItemModel(
            RESET_CUSTOM_THEMES_STORAGE,
            resourceManager.getString(R.string.debug_reset_custom_themes_storage_setting_name),
        )

        val addRandomPassword = TextSettingsListItemModel(
            ADD_RANDOM_PASSWORD_SETTING_ID,
            resourceManager.getString(
                R.string.debug_add_random_password_setting_name,
                passwordsRepository.getPasswordsCount(),
            )
        )

        val addRandomAccount = TextSettingsListItemModel(
            ADD_RANDOM_ACCOUNT_SETTING_ID,
            resourceManager.getString(
                R.string.debug_add_random_account_setting_name,
                accountsRepository.getAccountsCount(),
            )
        )

        return@withContext listOfNotNull(
            switchThemePeriodically,
            resetAccountsFeatureNewBadge,
            resetFingerprintFeatureNewBadge,
            resetAppThemeFeatureNewBadge,
            resetBackupFeatureNewBadge,
            resetPasswordsStorage,
            resetAccountsStorage,
            resetCustomThemesStorage,
            addRandomPassword,
            addRandomAccount,
        )
    }

    private companion object {
        const val SWITCH_THEME_PERIODICALLY_SETTING_ID = 0
        const val RESET_ACCOUNTS_FEATURE_NEW_BADGE_SETTING_ID = SWITCH_THEME_PERIODICALLY_SETTING_ID + 1
        const val RESET_FINGERPRINT_FEATURE_NEW_BADGE_SETTING_ID = RESET_ACCOUNTS_FEATURE_NEW_BADGE_SETTING_ID + 1
        const val RESET_APP_THEME_FEATURE_NEW_BADGE_SETTING_ID = RESET_FINGERPRINT_FEATURE_NEW_BADGE_SETTING_ID + 1
        const val RESET_BACKUP_FEATURE_NEW_BADGE_SETTING_ID = RESET_APP_THEME_FEATURE_NEW_BADGE_SETTING_ID + 1
        const val RESET_PASSWORDS_STORAGE_SETTING_ID = RESET_BACKUP_FEATURE_NEW_BADGE_SETTING_ID + 1
        const val RESET_ACCOUNTS_STORAGE_SETTING_ID = RESET_PASSWORDS_STORAGE_SETTING_ID + 1
        const val RESET_CUSTOM_THEMES_STORAGE = RESET_ACCOUNTS_STORAGE_SETTING_ID + 1
        const val ADD_RANDOM_PASSWORD_SETTING_ID = RESET_CUSTOM_THEMES_STORAGE + 1
        const val ADD_RANDOM_ACCOUNT_SETTING_ID = ADD_RANDOM_PASSWORD_SETTING_ID + 1

        val encryptedStrings = listOf(
            "_jU5NI2RAYuR6sQhjyUEOt6aYIs_dh-MWENtTMtVzGufyeGVU96TdzOUBBRYrj08G8YGARMxVEmN xyY8elsETdgJ4-Nk5hl4GUbscJgzz-Y=",
            "PSS4_a3W8bmtgVF9SBb9fYB84husgTbqIe8BZuYt-O-DzJdxAMpYgLbfnDW-YcfvacW42yErPGsN JxJcbrCmBnp68Lh6n0DNAYNc-pEIxUc=",
            "CrZem0O8s4SAfA0dMwBjJO92RTgVKvxWE4HAPFI_GljY8A98s71MJdB45SPV7FuDn63omVI_s1n2 _WY1yIyRlg==",
            "-PMbhW75CkuciWLfzknAX08JJboFc3lP9yfixFs6T8iDlqyelr7z6fWllYW5ki-oX6IMv7AAxl8Z 99zAfclD55FqBLk4jPuX0NEUhgGDU5A=",
            "5NS42gWAoJzAaPZwkfxr34etdQdgCDq_BqKkilAaRccGn7vKEgfHDe2a_rAWM1v4kVI2ne32MmqJ uVkW8cBZMpzFFc2Q71mUej00EFsXeakob5utybUMG_KC87cA3QqC",
        )

        fun randomInt() = Random.nextInt(from = 1_000, until = 100_000)

        fun generateRandomPassword(): PasswordRepoEntity {
            return object : PasswordRepoEntity {
                override fun getPasswordId(): Long? = null
                override fun getPasswordAvatarPath() = ""
                override fun getPasswordName() = "Password_${randomInt()}"
                override fun getPasswordContent() = encryptedStrings.random()
                override fun getPasswordPinTimestamp(): Long? = null
            }
        }

        fun generateRandomAccount(): AccountRepoEntity {
            return object : AccountRepoEntity {
                override fun getAccountId(): Long? = null
                override fun getAccountName() = "Account_${randomInt()}"
                override fun getAccountLogin() = encryptedStrings.random()
                override fun getAccountPassword() = encryptedStrings.random()
                override fun getAccountPinTimestamp(): Long? = null
            }
        }
    }
}