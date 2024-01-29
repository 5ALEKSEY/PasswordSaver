package com.ak.app_theme.theme

import android.content.Context
import androidx.annotation.AttrRes
import androidx.annotation.FloatRange
import com.ak.app_theme.R
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

// TODO: Should be providable with dagger
class CustomThemeManager private constructor() {
    companion object {
        const val NATIVE_THEME_ID = 0

        // Light themes
        const val BLUE_THEME_ID = 100
        const val GREEN_THEME_ID = 101

        // Dark themes
        const val ORANGE_THEME_ID = 200
        const val PURPLE_THEME_ID = 201
        const val TEAL_THEME_ID = 202

        const val DEFAULT_THEME_ID = BLUE_THEME_ID

        private val instance = CustomThemeManager()

        @JvmStatic
        fun getInstance() = instance

        @JvmStatic
        fun getCurrentSelectedTheme() = instance.getSelectedTheme()

        @JvmStatic
        fun getCurrentAppliedTheme() = instance.getAppliedTheme()

        @JvmStatic
        fun getThemeColor(@AttrRes attrRes: Int) = instance.getColor(attrRes)

        @JvmStatic
        fun getThemeColor(@AttrRes attrRes: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float) =
            instance.getColor(attrRes, alpha)
    }

    private val changeThemePublishSubject = PublishSubject.create<CustomTheme>()
    private val themes = mutableSetOf<CustomTheme>()
    private lateinit var currentTheme: CustomTheme
    private var userThemesProvider: ICustomUserThemesProvider? = null

    fun getSelectedTheme() = findThemeInternal(CustomThemePreferencesMng.getThemeId())

    fun getAppliedTheme() = currentTheme

    fun getAvailableThemes() = themes

    fun getChangeThemeListener(): Observable<CustomTheme> = changeThemePublishSubject

    fun refreshTheme(context: Context) {
        currentTheme = findTheme(CustomThemePreferencesMng.getThemeId(), context)
    }

    fun setTheme(themeId: Int, context: Context) {
        CustomThemePreferencesMng.setThemeId(themeId)
        refreshTheme(context)
        changeThemePublishSubject.onNext(currentTheme)
    }

    fun setNextTheme(context: Context) {
        val currentThemePosition = themes.indexOf(currentTheme)
        val nextThemePositionCandidate = if (currentThemePosition == -1) {
            themes.indexOfFirst { it.id == DEFAULT_THEME_ID }
        } else {
            (currentThemePosition + 1) % themes.size
        }

        val nextThemePosition = if (themes.elementAt(nextThemePositionCandidate).isNative) {
            nextThemePositionCandidate + 1
        } else {
            nextThemePositionCandidate
        }

        setTheme(themes.elementAt(nextThemePosition).id, context)
    }

    fun getColor(@AttrRes attrRes: Int) = currentTheme.getColor(attrRes)
    fun getColor(@AttrRes attrRes: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float) =
        currentTheme.getColor(attrRes, alpha)

    private fun findTheme(themeId: Int, context: Context): CustomTheme {
        return if (themeId != NATIVE_THEME_ID) {
            findThemeInternal(themeId)
        } else {
            val nativeThemeId = if (context.isDarkNativeUiMode()) {
                CustomThemePreferencesMng.getNativeDarkThemeId()
            } else {
                CustomThemePreferencesMng.getNativeLightThemeId()
            }

            findThemeInternal(nativeThemeId)
        }
    }

    private fun findThemeInternal(themeId: Int): CustomTheme {
        return themes.find { it.id == themeId }
            ?: themes.find { it.id == DEFAULT_THEME_ID }
            ?: throw IllegalStateException("can't find default theme id($DEFAULT_THEME_ID). themes=$themes")
    }

    fun init(context: Context, customUserThemesProvider: ICustomUserThemesProvider) {
        CustomThemePreferencesMng.initialize(context.applicationContext)
        userThemesProvider = customUserThemesProvider

        initThemesList(context)
        refreshTheme(context)
    }

    fun initThemesList(context: Context) {
        themes.clear()

        themes.add(
            CustomTheme.Builder(context)
                .id(NATIVE_THEME_ID)
                .name(R.string.native_theme_name)
                .build()
        )

        themes.add(
            CustomTheme.Builder(context)
                .id(BLUE_THEME_ID)
                .name(R.string.blue_theme_name)
                .themeStyle(R.style.CustomTheme_Blue)
                .lightThemeFlag(true)
                .build()
        )

        themes.add(
            CustomTheme.Builder(context)
                .id(GREEN_THEME_ID)
                .name(R.string.green_theme_name)
                .themeStyle(R.style.CustomTheme_Green)
                .lightThemeFlag(true)
                .build()
        )

        themes.add(
            CustomTheme.Builder(context)
                .id(ORANGE_THEME_ID)
                .name(R.string.orange_theme_name)
                .themeStyle(R.style.CustomTheme_Orange)
                .lightThemeFlag(false)
                .build()
        )

        themes.add(
            CustomTheme.Builder(context)
                .id(PURPLE_THEME_ID)
                .name(R.string.purple_theme_name)
                .themeStyle(R.style.CustomTheme_Purple)
                .lightThemeFlag(false)
                .build()
        )

        themes.add(
            CustomTheme.Builder(context)
                .id(TEAL_THEME_ID)
                .name(R.string.teal_theme_name)
                .themeStyle(R.style.CustomTheme_Teal)
                .lightThemeFlag(false)
                .build()
        )

        userThemesProvider?.let { themes.addAll(it.provideCustomUserThemes()) }
    }
}