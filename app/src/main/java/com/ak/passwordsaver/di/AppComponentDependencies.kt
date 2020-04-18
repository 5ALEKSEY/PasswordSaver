package com.ak.passwordsaver.di

import com.ak.core_repo_api.intefaces.ISettingsPreferencesManager
import com.ak.feature_security_api.interfaces.IAuthCheckerStarter
import com.ak.feature_security_api.interfaces.IPSAuthManager

interface AppComponentDependencies {
    fun needsPsAuthManager(): IPSAuthManager
    fun needsAuthCheckerStarter(): IAuthCheckerStarter
    fun needsSettingsPreferencesManager(): ISettingsPreferencesManager
}