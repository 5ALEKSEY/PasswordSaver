package com.ak.feature_tabsettings_impl.adapter.items.themechange

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.applier.ComplexViewsApplier
import com.ak.app_theme.theme.applier.CustomThemeApplier
import com.ak.app_theme.theme.uicomponents.recyclerview.CustomThemeRecyclerViewHolder
import com.ak.base.adapter.AdapterDelegate
import com.ak.feature_tabsettings_impl.R
import com.ak.feature_tabsettings_impl.adapter.BaseSettingsViewHolder
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel

class ThemeChangeAdapterDelegate(
    private val viewType: Int,
    private val onThemeChanged: (newThemeId: Int) -> Unit,
    private val onAddTheme: (settingId: Int) -> Unit,
    private val onEditTheme: (themeId: Int) -> Unit,
    private val onDeleteTheme: (themeId: Int) -> Unit,
) : AdapterDelegate<SettingsListItemModel> {

    override fun isForViewType(item: SettingsListItemModel) = item is ThemeChangeSettingsListItemModel

    override fun getItemViewType() = viewType

    override fun onCreateViewHolder(parent: ViewGroup): CustomThemeRecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.settings_item_theme_change_layout, parent, false)
        return ThemeChangeViewHolder(itemView, onThemeChanged, onAddTheme, onEditTheme, onDeleteTheme)
    }

    override fun onBindViewHolder(
        item: SettingsListItemModel,
        viewHolder: CustomThemeRecyclerViewHolder,
        theme: CustomTheme,
    ) {
        val itemModel = item as ThemeChangeSettingsListItemModel
        val holder = viewHolder as ThemeChangeViewHolder
        holder.bindViewHolder(itemModel)
    }
}

class ThemeChangeViewHolder(
    itemView: View,
    private val onThemeChanged: (newThemeId: Int) -> Unit,
    private val onAddTheme: (settingId: Int) -> Unit,
    private val onEditTheme: (themeId: Int) -> Unit,
    private val onDeleteTheme: (themeId: Int) -> Unit,
) : BaseSettingsViewHolder<ThemeChangeSettingsListItemModel>(itemView) {

    // TODO: move change theme picture out of this VH
    private val changeThemePicture by lazy { itemView.findViewById<ImageView>(R.id.givChangeThemePicture) }
    private val themesDescriptionsList by lazy { itemView.findViewById<RecyclerView>(R.id.rvThemesDescriptionsList) }

    override fun applyTheme(theme: CustomTheme) {
        super.applyTheme(theme)
        changeThemePicture.setColorFilter(
            theme.getColor(R.attr.themedPrimaryColor),
            PorterDuff.Mode.MULTIPLY,
        )
        ComplexViewsApplier.applyForRecyclerView(themesDescriptionsList, theme)
        CustomThemeApplier.applyBackground(theme, themesDescriptionsList, R.attr.themedPrimaryBackgroundColor)
    }

    override fun setViewHolderData(itemModel: ThemeChangeSettingsListItemModel) {
        if (themesDescriptionsList.adapter == null) {
            themesDescriptionsList.adapter = ThemeChangeDescriptionsAdapter(
                itemModel.themes,
                onThemeChanged,
                { onAddTheme(itemModel.settingId) },
                onEditTheme,
                onDeleteTheme,
            )
        }
        changeThemePicture.isVisible = itemModel.shouldShowThemeExampleView
    }
}