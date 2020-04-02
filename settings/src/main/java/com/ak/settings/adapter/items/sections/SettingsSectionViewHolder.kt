package com.ak.settings.adapter.items.sections

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ak.base.adapter.AdapterDelegate
import com.ak.base.extensions.setSafeClickListener
import com.ak.settings.R
import com.ak.settings.adapter.BaseSettingsViewHolder
import com.ak.settings.adapter.items.SettingsListItemModel
import kotlinx.android.synthetic.main.settings_item_section_layout.view.*

class SectionAdapterDelegate(
    private val viewType: Int,
    private val onSectionSettingsClicked: (settingId: Int) -> Unit
) : AdapterDelegate<SettingsListItemModel> {

    override fun isForViewType(item: SettingsListItemModel) = item is SectionSettingsListItemModel

    override fun getItemViewType() = viewType

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.settings_item_section_layout, parent, false)
        return SettingsSectionHolder(
            itemView,
            onSectionSettingsClicked
        )
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

    override fun setViewHolderData(itemModel: SectionSettingsListItemModel) {
        itemView.ivSettingsSectionImage.setImageResource(itemModel.imageRes)
        itemView.setSafeClickListener { onSectionSettingsClicked(itemModel.settingId) }
    }
}