package com.ak.base.ui.recycler.decorator

import android.graphics.Canvas
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.applier.CustomThemeApplier
import com.ak.base.R

class PsDividerItemDecoration(
    private val settings: PsDividerItemDecorationSettings,
) : DividerItemDecoration(settings.context, LinearLayoutManager.VERTICAL), CustomTheme.Support {

    override fun applyTheme(theme: CustomTheme) {
        drawable?.let {
            CustomThemeApplier.setDrawableColor(it, theme.getColor(R.attr.themedDividerColor))
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingStart + settings.leftDecorationOffset
        val right = parent.width - parent.paddingEnd + settings.rightDecorationOffset

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            if (!settings.shouldDrawRule(i, childCount)) continue

            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin + settings.topDecorationOffset
            val bottom = top + (drawable?.intrinsicHeight ?: 0) + settings.bottomDecorationOffset

            drawable?.apply {
                setBounds(left, top, right, bottom)
                draw(c)
            }
        }
    }
}