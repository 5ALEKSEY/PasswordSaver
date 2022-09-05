package com.ak.app_theme.theme.uicomponents.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.CustomThemeManager

abstract class CustomThemeRecyclerViewAdapter<T : CustomThemeRecyclerViewHolder>(
    private var theme: CustomTheme = CustomThemeManager.getCurrentTheme()
) : RecyclerView.Adapter<T>(), CustomTheme.Support {

    abstract fun onBindViewHolder(theme: CustomTheme, viewHolder: T, position: Int)

    override fun onBindViewHolder(viewHolder: T, position: Int) {
        viewHolder.setTheme(theme)
        onBindViewHolder(theme, viewHolder, position)
    }

    open fun onBindViewHolder(
        theme: CustomTheme,
        holder: T,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
    }

    override fun onBindViewHolder(holder: T, position: Int, payloads: MutableList<Any>) {
        onBindViewHolder(theme, holder, position, payloads)
    }

    override fun applyTheme(theme: CustomTheme) {
        if (this.theme.id != theme.id) {
            this.theme = theme
            notifyDataSetChanged()
        }
    }
}