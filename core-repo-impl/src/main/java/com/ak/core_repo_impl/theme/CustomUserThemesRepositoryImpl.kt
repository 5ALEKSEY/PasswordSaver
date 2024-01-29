package com.ak.core_repo_impl.theme

import android.content.SharedPreferences
import com.ak.core_repo_api.intefaces.theme.CustomUserThemeRepoEntity
import com.ak.core_repo_api.intefaces.theme.ICustomUserThemesRepository
import com.ak.core_repo_impl.data.model.db.PSDatabase
import com.ak.core_repo_impl.data.model.db.entities.customtheme.CustomUserThemeDBEntity
import com.ak.core_repo_impl.data.model.mapper.mapToPreStoredEntitiesList
import com.ak.core_repo_impl.data.model.mapper.mapToRepoEntitiesList
import com.ak.core_repo_impl.data.model.mapper.mapToThemesDbEntitiesList
import com.ak.core_repo_impl.di.module.PreferencesModule
import javax.inject.Inject
import javax.inject.Named
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CustomUserThemesRepositoryImpl @Inject constructor(
    localStore: PSDatabase,
    @Named(PreferencesModule.CUSTOM_THEME_PREFERENCES)
    private val preferences: SharedPreferences,
) : ICustomUserThemesRepository {

    private val themesLocalStore = localStore.getCustomUserThemesDao()

    override fun subscribeToAllThemesChanges(): Flow<List<CustomUserThemeRepoEntity>> {
        return themesLocalStore.subscribeToAllThemeChanges()
    }

    override fun getAllThemes(): List<CustomUserThemeRepoEntity> {
        return themesLocalStore.getAllThemes()
    }

    override suspend fun getThemeById(themeId: Long) = withContext(Dispatchers.IO) {
        themesLocalStore.getThemeById(themeId)
    }

    override suspend fun deleteThemesByIds(themeIds: List<Long>) = withContext(Dispatchers.IO) {
        val entitiesToDelete = themeIds.map { CustomUserThemeDBEntity(it) }.toTypedArray()
        return@withContext themesLocalStore.deleteThemes(*entitiesToDelete) >= 0
    }

    override suspend fun addNewThemes(themeRepoEntities: List<CustomUserThemeRepoEntity>) = withContext(Dispatchers.IO) {
        val entitiesToAdd = themeRepoEntities.mapToThemesDbEntitiesList().toTypedArray()
        return@withContext themesLocalStore.insertNewThemes(*entitiesToAdd).isNotEmpty()
    }

    override suspend fun updateThemes(themeRepoEntities: List<CustomUserThemeRepoEntity>) = withContext(Dispatchers.IO) {
        val entitiesToUpdate = themeRepoEntities.mapToThemesDbEntitiesList().toTypedArray()
        return@withContext themesLocalStore.updateThemes(*entitiesToUpdate) >= 0
    }

    override suspend fun getNextThemeId() = withContext(Dispatchers.IO) {
        maxOf(START_POINT_CUSTOM_THEMES_ID, getMaxThemeId()) + 1
    }

    override suspend fun clearAll() = withContext(Dispatchers.IO) {
        themesLocalStore.clearThemes()
    }

    override fun preStoreThemes(entities: List<CustomUserThemeRepoEntity>) {
        val entitiesJson = runCatching {
            Json.encodeToString(entities.mapToPreStoredEntitiesList())
        }.getOrDefault("")

        preferences.edit().putString(PRE_STORED_THEMES, entitiesJson).apply()
    }

    override fun getPreStoredThemes(): List<CustomUserThemeRepoEntity> {
        val entitiesJson = preferences.getString(PRE_STORED_THEMES, "") ?: ""

        return runCatching {
            Json.decodeFromString<List<CustomUserThemePreStoredEntity>>(entitiesJson)
                .mapToRepoEntitiesList()
        }.getOrDefault(emptyList())
    }

    private suspend fun getMaxThemeId() = withContext(Dispatchers.IO) {
        themesLocalStore.getMaxThemeId() ?: 0L
    }

    companion object {
        private const val PRE_STORED_THEMES = "pre_stored_themes"
        private const val START_POINT_CUSTOM_THEMES_ID = 1000L
    }
}