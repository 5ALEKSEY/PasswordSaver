package com.example.feature_customthememanager_impl.managetheme.mapper

import com.ak.app_theme.theme.CustomTheme
import com.ak.core_repo_api.intefaces.theme.CustomUserThemeRepoEntity
import com.example.feature_customthememanager_impl.managetheme.adapter.ModificationItemModel

interface ICustomThemeMapper {
    suspend fun customThemeToModificationsList(
        theme: CustomTheme,
    ): List<ModificationItemModel>

    suspend fun modificationsToRepoEntity(
        themeId: Long,
        themeName: String,
        isLightTheme: Boolean,
        modifications: List<ModificationItemModel>,
    ): CustomUserThemeRepoEntity
}