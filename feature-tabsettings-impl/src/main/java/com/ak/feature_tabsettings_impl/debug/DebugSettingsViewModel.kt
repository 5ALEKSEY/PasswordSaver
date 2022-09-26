package com.ak.feature_tabsettings_impl.debug

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ak.base.livedata.SingleEventLiveData
import com.ak.base.viewmodel.BasePSViewModel
import com.ak.core_repo_api.intefaces.AccountRepoEntity
import com.ak.core_repo_api.intefaces.IAccountsRepository
import com.ak.core_repo_api.intefaces.IPasswordsRepository
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.core_repo_api.intefaces.PasswordRepoEntity
import com.ak.feature_appupdate_api.interfaces.IFeaturesUpdateManager
import com.ak.feature_tabsettings_impl.R
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel
import com.ak.feature_tabsettings_impl.adapter.items.switches.SwitchSettingsListItemModel
import com.ak.feature_tabsettings_impl.adapter.items.texts.TextSettingsListItemModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import kotlin.random.Random

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
                passwordsRepository.clearAll()
                    .observeOn(AndroidSchedulers.mainThread())
                    .doAfterSuccess { loadDebugSettings() }
                    .subscribe()
                    .let(this::bindDisposable)
            }
            RESET_ACCOUNTS_STORAGE_SETTING_ID -> {
                accountsRepository.clearAll()
                    .observeOn(AndroidSchedulers.mainThread())
                    .doAfterSuccess { loadDebugSettings() }
                    .subscribe()
                    .let(this::bindDisposable)
            }
            ADD_RANDOM_PASSWORD_SETTING_ID -> {
                passwordsRepository.addNewPasswords(listOf(generateRandomPassword()))
                    .observeOn(AndroidSchedulers.mainThread())
                    .doAfterSuccess { loadDebugSettings() }
                    .subscribe()
                    .let(this::bindDisposable)
            }
            ADD_RANDOM_ACCOUNT_SETTING_ID -> {
                accountsRepository.addNewAccounts(listOf(generateRandomAccount()))
                    .observeOn(AndroidSchedulers.mainThread())
                    .doAfterSuccess { loadDebugSettings() }
                    .subscribe()
                    .let(this::bindDisposable)
            }
            else -> {
                // no op
            }
        }
    }

    fun loadDebugSettings() {
        getDebugSettingsList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list -> debugSettingsListLiveData.value = list }
            .let(this::bindDisposable)
    }

    @WorkerThread
    private fun getDebugSettingsList(): Single<List<SettingsListItemModel>> {
        return Single.fromCallable {
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

            return@fromCallable listOfNotNull(
                switchThemePeriodically,
                resetAccountsFeatureNewBadge,
                resetFingerprintFeatureNewBadge,
                resetAppThemeFeatureNewBadge,
                resetPasswordsStorage,
                resetAccountsStorage,
                addRandomPassword,
                addRandomAccount,
            )
        }
    }

    private companion object {
        const val SWITCH_THEME_PERIODICALLY_SETTING_ID = 0
        const val RESET_ACCOUNTS_FEATURE_NEW_BADGE_SETTING_ID = 1
        const val RESET_FINGERPRINT_FEATURE_NEW_BADGE_SETTING_ID = 2
        const val RESET_APP_THEME_FEATURE_NEW_BADGE_SETTING_ID = 3
        const val RESET_PASSWORDS_STORAGE_SETTING_ID = 4
        const val RESET_ACCOUNTS_STORAGE_SETTING_ID = 5
        const val ADD_RANDOM_PASSWORD_SETTING_ID = 6
        const val ADD_RANDOM_ACCOUNT_SETTING_ID = 7

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
            }
        }

        fun generateRandomAccount(): AccountRepoEntity {
            return object : AccountRepoEntity {
                override fun getAccountId(): Long? = null
                override fun getAccountName() = "Account_${randomInt()}"
                override fun getAccountLogin() = encryptedStrings.random()
                override fun getAccountPassword() = encryptedStrings.random()
            }
        }
    }
}