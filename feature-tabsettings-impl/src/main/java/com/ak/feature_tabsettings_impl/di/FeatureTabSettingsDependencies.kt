package com.ak.feature_tabsettings_impl.di

import com.ak.core_repo_api.intefaces.IPasswordsRepository
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.core_repo_api.intefaces.ISettingsPreferencesManager
import com.ak.feature_appupdate_api.interfaces.IFeaturesUpdateManager
import com.ak.feature_security_api.interfaces.IAuthCheckerStarter
import com.ak.feature_security_api.interfaces.IPSBiometricManager

interface FeatureTabSettingsDependencies {
    fun needsPasswordsRepository(): IPasswordsRepository
    fun needsSettingsPreferencesManager(): ISettingsPreferencesManager
    fun needsAuthCheckerStarter(): IAuthCheckerStarter
    fun needsPSBiometricManager(): IPSBiometricManager
    fun needsFeaturesUpdateManager(): IFeaturesUpdateManager
    fun needsResourceManager(): IResourceManager
}