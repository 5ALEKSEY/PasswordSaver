package com.ak.core_repo_impl.data.model.db.entities.customtheme

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.ak.core_repo_api.intefaces.theme.CustomUserThemeRepoEntity
import com.ak.core_repo_api.intefaces.theme.ThemeColorAttr
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = CustomUserThemeDBEntity.TABLE_NAME)
@TypeConverters(CustomThemeColorAttrsConverter::class)
@Serializable
data class CustomUserThemeDBEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_THEME_ID)
    @SerialName(COLUMN_THEME_ID)
    var themeIdValue: Long,
    @ColumnInfo(name = COLUMN_NAME)
    @SerialName(COLUMN_NAME)
    var nameValue: String,
    @ColumnInfo(name = COLUMN_IS_LIGHT)
    @SerialName(COLUMN_IS_LIGHT)
    var isLightValue: Boolean,
    @ColumnInfo(name = COLUMN_COLOR_ATTRS)
    @SerialName(COLUMN_COLOR_ATTRS)
    var colorAttrsValue: List<ThemeColorAttr>,
) : CustomUserThemeRepoEntity {

    @Ignore
    constructor(themeId: Long) : this(
        themeId,
        "",
        false,
        emptyList(),
    )

    companion object {
        const val TABLE_NAME = "custom_user_themes"

        // column names
        const val COLUMN_THEME_ID = "theme_id" // primary key
        const val COLUMN_NAME = "name"
        const val COLUMN_IS_LIGHT = "is_light"
        const val COLUMN_COLOR_ATTRS = "color_attrs"
    }

    override fun getThemeId(): Long = themeIdValue

    override fun getName(): String = nameValue

    override fun isLight(): Boolean = isLightValue

    override fun getColorAttrs(): List<ThemeColorAttr> = colorAttrsValue
}