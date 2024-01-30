package com.example.feature_customthememanager_impl.di

import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.core_repo_api.intefaces.theme.ICustomUserThemesRepository

interface FeatureCustomThemeManagerDependencies {
    fun needsResourceManager(): IResourceManager
    fun needsCustomUserThemesRepo(): ICustomUserThemesRepository
}