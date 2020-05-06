package com.ak.feature_security_impl.di

import com.ak.core_repo_api.intefaces.IAuthPreferencesManager
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.core_repo_api.intefaces.ISettingsPreferencesManager

interface FeatureSecurityDependencies {
    fun needAuthPreferencesManager(): IAuthPreferencesManager
    fun needSettingsPreferences(): ISettingsPreferencesManager
    fun needsResourceManager(): IResourceManager
}