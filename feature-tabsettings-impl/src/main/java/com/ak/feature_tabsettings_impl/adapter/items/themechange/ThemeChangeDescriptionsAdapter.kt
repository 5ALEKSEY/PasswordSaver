package com.ak.feature_tabsettings_impl.adapter.items.themechange

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.uicomponents.recyclerview.CustomThemeRecyclerViewAdapter
import com.ak.app_theme.theme.uicomponents.recyclerview.CustomThemeRecyclerViewHolder
import com.ak.feature_tabsettings_impl.R

class ThemeChangeDescriptionsAdapter(
    private val descriptions: List<CustomTheme.Description>,
    private val onThemeChanged: (newThemeId: Int) -> Unit,
    private val onAddTheme: () -> Unit,
    private val onEditTheme: (themeId: Int) -> Unit,
    private val onDeleteTheme: (themeId: Int) -> Unit,
) : CustomThemeRecyclerViewAdapter<CustomThemeRecyclerViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        val themeDescription = descriptions[position]
        return when {
            themeDescription.id == -1 -> ADD_THEME_VIEW_TYPE // TODO: wrap adapter item to interface to resolve this
            themeDescription.isCustom -> USER_CUSTOM_THEME_VIEW_TYPE
            else -> APP_THEME_VIEW_TYPE
        }
    }

    override fun onBindViewHolder(
        theme: CustomTheme,
        viewHolder: CustomThemeRecyclerViewHolder,
        position: Int,
    ) {
        when (viewHolder) {
            is AppThemeDescriptionViewHolder -> viewHolder.bind(descriptions[position])
            is CustomThemeDescriptionViewHolder -> viewHolder.bind(descriptions[position])
            is AddThemeDescriptionViewHolder -> viewHolder.bind()
            else -> {
                // np
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CustomThemeRecyclerViewHolder {
        val layoutToInflate = when (viewType) {
            APP_THEME_VIEW_TYPE -> R.layout.settings_item_app_theme_description_layout
            USER_CUSTOM_THEME_VIEW_TYPE -> R.layout.settings_item_custom_theme_description_layout
            else -> R.layout.settings_item_add_theme_description_layout
        }
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(layoutToInflate, parent, false)
        return when (viewType) {
            APP_THEME_VIEW_TYPE -> AppThemeDescriptionViewHolder(itemView, onThemeChanged)
            USER_CUSTOM_THEME_VIEW_TYPE -> CustomThemeDescriptionViewHolder(
                itemView,
                onThemeChanged,
                onEditTheme,
                onDeleteTheme,
            )
            else -> AddThemeDescriptionViewHolder(itemView, onAddTheme)
        }
    }

    override fun getItemCount() = descriptions.size

    private companion object {
        const val APP_THEME_VIEW_TYPE = 0
        const val USER_CUSTOM_THEME_VIEW_TYPE = APP_THEME_VIEW_TYPE + 1
        const val ADD_THEME_VIEW_TYPE = USER_CUSTOM_THEME_VIEW_TYPE + 1
    }
}
