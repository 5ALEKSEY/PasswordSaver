package com.example.feature_customthememanager_impl.managetheme.adapter.delegates

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.applier.CustomThemeApplier
import com.ak.app_theme.theme.uicomponents.recyclerview.CustomThemeRecyclerViewHolder
import com.ak.base.adapter.AdapterDelegate
import com.ak.feature_customthememanager_impl.R
import com.example.feature_customthememanager_impl.managetheme.adapter.ColorModificationItemModel
import com.example.feature_customthememanager_impl.managetheme.adapter.ModificationItemModel

class ColorModificationAdapterDelegate(
    private val viewType: Int,
    private val onChangeColor: (itemId: Int, previousColorValue: Int) -> Unit,
) : AdapterDelegate<ModificationItemModel> {

    override fun isForViewType(item: ModificationItemModel) = item is ColorModificationItemModel

    override fun getItemViewType() = viewType

    override fun onCreateViewHolder(parent: ViewGroup): CustomThemeRecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.color_modification_item_layout, parent, false)
        return ColorModificationViewHolder(itemView, onChangeColor)
    }

    override fun onBindViewHolder(
        item: ModificationItemModel,
        viewHolder: CustomThemeRecyclerViewHolder,
        theme: CustomTheme,
    ) {
        val itemModel = item as ColorModificationItemModel
        val holder = viewHolder as ColorModificationViewHolder
        holder.bind(itemModel)
    }

}

class ColorModificationViewHolder(
    itemView: View,
    private val onChangeColor: (itemId: Int, previousColorValue: Int) -> Unit,
) : CustomThemeRecyclerViewHolder(itemView) {

    private val name by lazy { itemView.findViewById<TextView>(R.id.tvColorToModifyName) }
    private val example by lazy { itemView.findViewById<ImageView>(R.id.ivColorToModifyExample) }

    override fun applyTheme(theme: CustomTheme) {
        CustomThemeApplier.applyTextColor(theme, name, R.attr.themedPrimaryTextColor)
    }

    fun bind(item: ColorModificationItemModel) {
        itemView.setOnClickListener { onChangeColor(item.itemId, item.colorIntValue) }
        example.setImageDrawable(ColorDrawable(item.colorIntValue))
        name.text = item.itemName
    }
}