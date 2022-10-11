package com.ak.app_theme.theme.uicomponents.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ak.app_theme.theme.CustomTheme

abstract class CustomThemeRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var themeId = -1
    private var themedViews: MutableList<CustomTheme.Support>? = null

    protected abstract fun applyTheme(theme: CustomTheme)

    fun setTheme(theme: CustomTheme) {
        if (themeId != theme.id) {
            themedViews?.forEach { it.applyTheme(theme) }
            applyTheme(theme)
            themeId = theme.id
        }
    }

    fun addThemedView(view: CustomTheme.Support?) {
        view?.let {
            if (themedViews == null) {
                themedViews = mutableListOf()
            }

            themedViews?.add(it)
        }
    }
}