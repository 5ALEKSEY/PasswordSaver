package com.example.feature_customthememanager_impl.managetheme.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.uicomponents.recyclerview.CustomThemeRecyclerViewAdapter
import com.ak.app_theme.theme.uicomponents.recyclerview.CustomThemeRecyclerViewHolder
import com.ak.base.adapter.AdapterDelegatesManager
import com.example.feature_customthememanager_impl.managetheme.adapter.delegates.ColorModificationAdapterDelegate

class ThemeModificationRecyclerViewAdapter constructor(
    onChangeColor: (itemId: Int, previousColorValue: Int) -> Unit,
) : CustomThemeRecyclerViewAdapter<CustomThemeRecyclerViewHolder>() {

    private val modificationsList = arrayListOf<ModificationItemModel>()
    private val adapterDelegatesManager = AdapterDelegatesManager<ModificationItemModel>()

    init {
        adapterDelegatesManager.addDelegate(
            ColorModificationAdapterDelegate(
                viewType = CHANGE_COLOR_TYPE,
                onChangeColor = onChangeColor,
            )
        )
    }

    fun submitNewItems(newItems: List<ModificationItemModel>) {
        val diffCallback = ThemeModificationDiffUtilCallback(
            oldList = modificationsList,
            newList = newItems,
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        modificationsList.clear()
        modificationsList.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomThemeRecyclerViewHolder {
        return adapterDelegatesManager.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(
        theme: CustomTheme,
        viewHolder: CustomThemeRecyclerViewHolder,
        position: Int,
    ) {
        adapterDelegatesManager.onBindViewHolder(modificationsList[position], viewHolder, theme)
    }

    override fun getItemViewType(position: Int): Int {
        return adapterDelegatesManager.getItemViewType(modificationsList[position])
    }

    override fun getItemCount() = modificationsList.size

    class ThemeModificationDiffUtilCallback(
        private val oldList: List<ModificationItemModel>,
        private val newList: List<ModificationItemModel>,
    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].itemId == newList[newItemPosition].itemId

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            val isSameId = oldItem.itemId == newItem.itemId
            if (oldItem is ColorModificationItemModel && newItem is ColorModificationItemModel) {
                return oldItem == newItem
            }
            return isSameId
        }
    }

    companion object {
        const val CHANGE_COLOR_TYPE = 0
    }
}