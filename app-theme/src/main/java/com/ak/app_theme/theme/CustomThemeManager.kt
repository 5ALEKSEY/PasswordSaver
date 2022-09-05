package com.ak.app_theme.theme

import android.content.Context
import androidx.annotation.AttrRes
import androidx.annotation.FloatRange
import com.ak.app_theme.R
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class CustomThemeManager private constructor() {
    companion object {
        const val BLUE_THEME_ID = 0
        const val ORANGE_THEME_ID = 1
        const val DEFAULT_THEME_ID = BLUE_THEME_ID

        private val instance = CustomThemeManager()

        @JvmStatic
        fun getInstance() = instance

        @JvmStatic
        fun getCurrentTheme() = instance.getTheme()

        @JvmStatic
        fun getThemeColor(@AttrRes attrRes: Int) = instance.getColor(attrRes)

        @JvmStatic
        fun getThemeColor(@AttrRes attrRes: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float) =
            instance.getColor(attrRes, alpha)
    }

    private val changeThemePublishSubject = PublishSubject.create<CustomTheme>()
    private val themes = mutableListOf<CustomTheme>()
    private lateinit var currentTheme: CustomTheme

    fun getAvailableThemes() = themes.map {
        CustomTheme.Description(it.id, it.nameResId)
    }

    fun getChangeThemeListener(): Observable<CustomTheme> = changeThemePublishSubject

    fun getTheme() = currentTheme
    fun setTheme(themeId: Int) = applyThemeAndNotify(findTheme(themeId))
    fun setNextTheme() = applyNextThemeAndNotify()

    private fun applyThemeAndNotify(theme: CustomTheme) {
        currentTheme = theme
        savePrefsThemeId(theme.id)
        changeThemePublishSubject.onNext(theme)
    }

    private fun applyNextThemeAndNotify() {
        val nextThemeId = (currentTheme.id + 1) % themes.size
        applyThemeAndNotify(findTheme(nextThemeId))
    }

    fun getColor(@AttrRes attrRes: Int) = currentTheme.getColor(attrRes)
    fun getColor(@AttrRes attrRes: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float) =
        currentTheme.getColor(attrRes, alpha)

    private fun getPrefsThemeId() = CustomThemePreferencesMng.getThemeId()

    private fun savePrefsThemeId(id: Int) = CustomThemePreferencesMng.setThemeId(id)

    private fun findTheme(themeId: Int) = themes.find { it.id == themeId }
        ?: throw IllegalStateException("findTheme=> there is no theme with id = $themeId")

    fun refreshTheme(context: Context) {
        val themeId = getPrefsThemeId()
        currentTheme = findTheme(themeId)
    }

    fun init(context: Context) {
        CustomThemePreferencesMng.initialize(context.applicationContext)

        themes.add(
            CustomTheme.Builder(context)
                .id(BLUE_THEME_ID)
                .name(R.string.blue_theme_name)
                .themeStyle(R.style.CustomTheme_Blue)
                .build()
        )

        themes.add(
            CustomTheme.Builder(context)
                .id(ORANGE_THEME_ID)
                .name(R.string.orange_theme_name)
                .themeStyle(R.style.CustomTheme_Orange)
                .build()
        )

        refreshTheme(context)
    }
}