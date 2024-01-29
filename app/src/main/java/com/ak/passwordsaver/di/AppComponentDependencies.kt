package com.ak.passwordsaver.di

import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.core_repo_api.intefaces.preference.ISettingsPreferencesManager
import com.ak.core_repo_api.intefaces.theme.ICustomUserThemesRepository
import com.ak.feature_appupdate_api.interfaces.IFeaturesUpdateManager
import com.ak.feature_security_api.interfaces.IAuthCheckerStarter
import com.ak.feature_security_api.interfaces.IPSAuthManager

interface AppComponentDependencies {
    fun needsPsAuthManager(): IPSAuthManager
    fun needsAuthCheckerStarter(): IAuthCheckerStarter
    fun needsSettingsPreferencesManager(): ISettingsPreferencesManager
    fun needsFeaturesUpdateManager(): IFeaturesUpdateManager
    fun needsResourceManager(): IResourceManager
    fun needsCustomUserThemesRepository(): ICustomUserThemesRepository
}