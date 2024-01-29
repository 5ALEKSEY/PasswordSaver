package com.ak.core_repo_api.intefaces.theme

import kotlinx.coroutines.flow.Flow

interface ICustomUserThemesRepository {
    fun subscribeToAllThemesChanges(): Flow<List<CustomUserThemeRepoEntity>>
    fun getAllThemes(): List<CustomUserThemeRepoEntity>
    suspend fun getThemeById(themeId: Long): CustomUserThemeRepoEntity?
    suspend fun deleteThemesByIds(themeIds: List<Long>): Boolean
    suspend fun addNewThemes(themeRepoEntities: List<CustomUserThemeRepoEntity>): Boolean
    suspend fun updateThemes(themeRepoEntities: List<CustomUserThemeRepoEntity>): Boolean
    suspend fun getNextThemeId(): Long
    suspend fun clearAll()


    fun preStoreThemes(entities: List<CustomUserThemeRepoEntity>)
    fun getPreStoredThemes(): List<CustomUserThemeRepoEntity>
}