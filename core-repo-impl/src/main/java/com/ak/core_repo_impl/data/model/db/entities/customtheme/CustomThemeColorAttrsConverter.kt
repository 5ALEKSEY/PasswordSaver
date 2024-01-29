package com.ak.core_repo_impl.data.model.db.entities.customtheme

import androidx.room.TypeConverter
import com.ak.core_repo_api.intefaces.theme.ThemeColorAttr
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CustomThemeColorAttrsConverter {

    @TypeConverter
    fun fromStringToThemeColorAttrs(value: String): List<ThemeColorAttr> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromThemeColorAttrsToString(value: List<ThemeColorAttr>): String {
        return Json.encodeToString(value)
    }
}