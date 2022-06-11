package com.ak.app_theme.theme

import android.content.Context
import android.content.SharedPreferences

object CustomThemePreferencesMng {
    private const val PREFERENCES_NAME = "THEME_PREFERENCES"
    private const val THEME_ID_KEY = "THEME_ID_KEY"

    private lateinit var sharedPreferences: SharedPreferences

    private val dataCache by lazy {
        mutableMapOf<String, Any>()
    }

    fun initialize(appContext: Context) {
        sharedPreferences = appContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun setThemeId(themeId: Int) {
        dataCache[THEME_ID_KEY] = themeId
        sharedPreferences.edit().putInt(THEME_ID_KEY, themeId).apply()
    }

    fun getThemeId() = dataCache.getOrDefaultWithExpression(
        THEME_ID_KEY,
        {
            val result = sharedPreferences.getInt(
                THEME_ID_KEY, CustomThemeManager.DEFAULT_THEME_ID
            )
            dataCache[THEME_ID_KEY] = result
            return@getOrDefaultWithExpression result
        }
    )

    @Suppress("UNCHECKED_CAST")
    private fun <K, V, C> Map<K, V>.getOrDefaultWithExpression(key: K, exp: () -> C) = if (contains(key)) {
        this[key] as C
    } else {
        exp()
    }
}