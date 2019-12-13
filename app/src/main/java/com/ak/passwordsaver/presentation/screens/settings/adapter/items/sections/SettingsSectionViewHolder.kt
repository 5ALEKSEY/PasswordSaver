package com.ak.passwordsaver.presentation.screens.settings.adapter.items.sections

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.base.adapter.AdapterDelegate
import com.ak.passwordsaver.presentation.screens.settings.adapter.BaseSettingsViewHolder
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.SettingsListItemModel
import com.ak.passwordsaver.utils.bindView
import com.ak.passwordsaver.utils.extensions.setSafeClickListener

class SectionAdapterDelegate(
    private val viewType: Int,
    private val onSectionSettingsClicked: (settingId: Int) -> Unit
) : AdapterDelegate<SettingsListItemModel> {

    override fun isForViewType(item: SettingsListItemModel) = item is SectionSettingsListItemModel

    override fun getItemViewType() = viewType

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.settings_item_section_layout, parent, false)
        return SettingsSectionHolder(itemView, onSectionSettingsClicked)
    }

    override fun onBindViewHolder(
        item: SettingsListItemModel,
        viewHolder: RecyclerView.ViewHolder
    ) {
        val itemModel = item as SectionSettingsListItemModel
        val holder = viewHolder as SettingsSectionHolder
        holder.bindViewHolder(itemModel)
    }
}

class SettingsSectionHolder(
    itemView: View,
    private val onSectionSettingsClicked: (settingId: Int) -> Unit
) : BaseSettingsViewHolder<SectionSettingsListItemModel>(itemView) {

    private val mSectionImageView: ImageView by bindView(R.id.iv_settings_section_image)

    override fun setViewHolderData(itemModel: SectionSettingsListItemModel) {
        mSectionImageView.setImageResource(itemModel.imageRes)
        itemView.setSafeClickListener { onSectionSettingsClicked(itemModel.settingId) }
    }
}