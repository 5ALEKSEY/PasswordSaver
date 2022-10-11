package com.ak.app_theme.theme.applier

import android.content.res.ColorStateList
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.CustomThemedView
import com.google.android.material.floatingactionbutton.FloatingActionButton

object ComplexViewsApplier {

    private const val TAG = "ComplexViewsApplier"

    fun canApplyBackgroundTint(view: View?): Boolean {
        return view is FloatingActionButton
    }

    fun applyForComplexView(
        themedView: CustomThemedView,
        view: View?,
        theme: CustomTheme,
    ) {
        when (view) {
            is RecyclerView -> applyForRecyclerView(view, theme)
            is FloatingActionButton -> applyForFloatingActionButton(view, themedView, theme)
            is CustomTheme.Support -> view.applyTheme(theme)
            else -> {
                // no op
            }
        }
    }

    fun applyForRecyclerView(
        view: RecyclerView?,
        theme: CustomTheme,
    ): Boolean {
        if (view == null) {
            Log.w(TAG, "applyForRecyclerView. view is null")
            return false
        }

        if (view.adapter is CustomTheme.Support) {
            (view.adapter as CustomTheme.Support).applyTheme(theme)
        }

        for (index in 0 until view.itemDecorationCount) {
            val decoration = view.getItemDecorationAt(index)
            if (decoration is CustomTheme.Support) {
                decoration.applyTheme(theme)
            }
        }

        view.invalidateItemDecorations()
        return true
    }

    fun applyForFloatingActionButton(
        view: FloatingActionButton?,
        themedView: CustomThemedView,
        theme: CustomTheme,
    ): Boolean {
        if (view == null) {
            Log.w(TAG, "applyForFloatingActionButton. view is null")
            return false
        }

        themedView.getBackgroundTintResource()?.let { bgResAttr ->
            view.backgroundTintList = ColorStateList.valueOf(theme.getColor(bgResAttr))
        }

        return true
    }
}