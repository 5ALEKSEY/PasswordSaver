package com.ak.feature_tabsettings_impl.design.configurenative

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.CustomThemeManager
import com.ak.app_theme.theme.applier.CustomThemeApplier
import com.ak.app_theme.theme.selectors.CustomThemeDrawableSelectorBuilder
import com.ak.app_theme.theme.uicomponents.recyclerview.CustomThemeRecyclerViewAdapter
import com.ak.app_theme.theme.uicomponents.recyclerview.CustomThemeRecyclerViewHolder
import com.ak.feature_tabsettings_impl.R

class ConfigureNativeThemeDescriptionsAdapter(
    initialSelectedDescriptionId: Int,
    private val descriptions: List<CustomTheme.Description>,
) : CustomThemeRecyclerViewAdapter<ConfigureNativeThemeDescriptionsAdapter.ThemeChangeDescriptionViewHolder>() {

    var selectedDescriptionId = initialSelectedDescriptionId

    private val onThemeChangedListener = { newTheme: CustomTheme.Description ->
        selectedDescriptionId = newTheme.id
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(
        theme: CustomTheme,
        viewHolder: ThemeChangeDescriptionViewHolder,
        position: Int,
    ) {
        viewHolder.bind(descriptions[position])
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ThemeChangeDescriptionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(
            R.layout.settings_item_app_theme_description_layout,
            parent,
            false,
        )
        return ThemeChangeDescriptionViewHolder(itemView)
    }

    override fun getItemCount() = descriptions.size

    inner class ThemeChangeDescriptionViewHolder(itemView: View) : CustomThemeRecyclerViewHolder(itemView) {

        private val root by lazy { itemView.findViewById<View>(R.id.cvThemeChangeDescriptionRoot) }
        private val content by lazy { itemView.findViewById<View>(R.id.clThemeChangeDescriptionContent) }
        private val themeName by lazy { itemView.findViewById<TextView>(R.id.tvThemeDescriptionName) }
        private val impressionColor1 by lazy { itemView.findViewById<ImageView>(R.id.civThemeDescriptionImpressionColor1) }
        private val impressionColor2 by lazy { itemView.findViewById<ImageView>(R.id.civThemeDescriptionImpressionColor2) }
        private val impressionColor3 by lazy { itemView.findViewById<ImageView>(R.id.civThemeDescriptionImpressionColor3) }

        override fun applyTheme(theme: CustomTheme) {
            CustomThemeApplier.applyTextColor(theme, themeName, R.attr.themedPrimaryTextColor)
            CustomThemeApplier.applyBackgroundTint(theme, root, R.attr.themedSecondaryBackgroundColor)
        }

        fun bind(themeDescription: CustomTheme.Description) {
            itemView.setOnClickListener { onThemeChangedListener(themeDescription) }
            themeName.text = themeDescription.getName(itemView.context)
            content.background = getThemeDescriptionBackground(themeDescription)

            impressionColor1.setImageDrawable(ColorDrawable(themeDescription.impressColor1))
            impressionColor2.setImageDrawable(ColorDrawable(themeDescription.impressColor2))
            impressionColor3.setImageDrawable(ColorDrawable(themeDescription.impressColor3))
        }

        // Not really good approach, but ...... fast and simple :)
        private fun getThemeDescriptionBackground(themeDescription: CustomTheme.Description): Drawable? {
            return if (themeDescription.id == selectedDescriptionId) {
                CustomThemeDrawableSelectorBuilder(CustomThemeManager.getCurrentAppliedTheme(), itemView.context)
                    .rectangle()
                    .setDefaultColorAttr(R.attr.themedSecondaryBackgroundColor)
                    .setDefaultStrokeColorAttr(R.attr.themedAccentColor)
                    .setDefaultStrokeWidthDp(1F)
                    .setDefaultRadiusDp(15F)
                    .build()
            } else {
                null
            }
        }
    }
}