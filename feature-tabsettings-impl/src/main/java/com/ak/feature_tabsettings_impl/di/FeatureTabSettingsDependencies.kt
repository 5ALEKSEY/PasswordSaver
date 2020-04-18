package com.ak.feature_tabsettings_impl.di

import com.ak.core_repo_api.intefaces.IPasswordsRepository
import com.ak.core_repo_api.intefaces.ISettingsPreferencesManager
import com.ak.feature_security_api.interfaces.IAuthCheckerStarter

interface FeatureTabSettingsDependencies {
    fun needsPasswordsRepository(): IPasswordsRepository
    fun needsSettingsPreferencesManager(): ISettingsPreferencesManager
    fun needsAuthCheckerStarter(): IAuthCheckerStarter
}