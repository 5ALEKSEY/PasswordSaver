package com.ak.core_repo_impl.data.model.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ak.core_repo_impl.data.model.db.entities.customtheme.CustomUserThemeDBEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomUserThemesDao {

    @Query("SELECT * FROM ${CustomUserThemeDBEntity.TABLE_NAME}")
    fun subscribeToAllThemeChanges(): Flow<List<CustomUserThemeDBEntity>>

    @Query("SELECT * FROM ${CustomUserThemeDBEntity.TABLE_NAME}")
    fun getAllThemes(): List<CustomUserThemeDBEntity>

    @Query("SELECT * FROM ${CustomUserThemeDBEntity.TABLE_NAME} " +
               "WHERE ${CustomUserThemeDBEntity.COLUMN_THEME_ID} = :themeId " +
               "LIMIT 1")
    suspend fun getThemeById(themeId: Long): CustomUserThemeDBEntity?

    @Query("SELECT MAX(${CustomUserThemeDBEntity.COLUMN_THEME_ID}) " +
               "FROM ${CustomUserThemeDBEntity.TABLE_NAME}")
    suspend fun getMaxThemeId(): Long?

    @Query("DELETE FROM ${CustomUserThemeDBEntity.TABLE_NAME}")
    suspend fun clearThemes()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewThemes(vararg themeDBEntity: CustomUserThemeDBEntity): List<Long>

    @Delete
    suspend fun deleteThemes(vararg themeDBEntities: CustomUserThemeDBEntity): Int

    @Update
    suspend fun updateThemes(vararg themeDBEntities: CustomUserThemeDBEntity): Int
}