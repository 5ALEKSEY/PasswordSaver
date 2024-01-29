package com.ak.passwordsaver.userthemesprovider

import android.content.Context
import com.ak.app_theme.R
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.CustomThemeColorAttr
import com.ak.app_theme.theme.ICustomUserThemesProvider
import com.ak.base.extensions.hexToIntColor
import com.ak.core_repo_api.intefaces.theme.ICustomUserThemesRepository
import javax.inject.Inject

class CustomUserThemesProviderImpl @Inject constructor(
    private val appContext: Context,
    private val customThemesRepo: ICustomUserThemesRepository,
) : ICustomUserThemesProvider {

    override fun provideCustomUserThemes(): List<CustomTheme> {
        return customThemesRepo.getPreStoredThemes().map { customUserTheme ->
            val themeStyle = if (customUserTheme.isLight()) {
                R.style.CustomTheme_Blue
            } else {
                R.style.CustomTheme_Orange
            }
            val builder = CustomTheme.Builder(appContext)
                .id(customUserTheme.getThemeId().toInt())
                .name(customUserTheme.getName())
                .themeStyle(themeStyle)
                .lightThemeFlag(customUserTheme.isLight())

            customUserTheme.getColorAttrs().forEach { customColorAttr ->
                builder.overrideColorAttr(
                    CustomThemeColorAttr.customIdToAndroid(customColorAttr.attrCustomId),
                    customColorAttr.colorHexValue.hexToIntColor(),
                )
            }

            return@map builder.build()
        }
    }
}