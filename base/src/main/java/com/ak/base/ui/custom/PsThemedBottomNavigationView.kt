package com.ak.base.ui.custom

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.selectors.CustomThemeColorSelectorBuilder
import com.ak.base.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class PsThemedBottomNavigationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : BottomNavigationView(context, attrs), CustomTheme.Support {

    override fun applyTheme(theme: CustomTheme) {
        getItemColorStateList(theme)?.let { itemColor ->
            itemIconTintList = itemColor
            itemTextColor = itemColor
        }
    }

    private fun getItemColorStateList(theme: CustomTheme): ColorStateList? {
        return CustomThemeColorSelectorBuilder(theme)
            .setDefaultColorAttr(R.attr.themedSecondaryTextColor)
            .setCheckedColorAttr(R.attr.themedPrimaryColor)
            .build()
    }
}