package com.ak.core_repo_api.intefaces.theme

interface CustomUserThemeRepoEntity {
    fun getThemeId(): Long
    fun getName(): String
    fun isLight(): Boolean
    fun getColorAttrs(): List<ThemeColorAttr>
}