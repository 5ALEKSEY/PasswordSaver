package com.ak.base.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.uicomponents.recyclerview.CustomThemeRecyclerViewHolder

class AdapterDelegatesManager<T> {
    private val mAdapterDelegates = arrayListOf<AdapterDelegate<T>>()

    fun addDelegate(delegate: AdapterDelegate<T>) {
        mAdapterDelegates.add(delegate)
    }

    fun getItemViewType(item: T): Int {
        val delegateForViewType = getDelegateForItem(item)
            ?: throw NullPointerException("No delegate found for item: ${item.toString()}")
        return delegateForViewType.getItemViewType()
    }

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomThemeRecyclerViewHolder {
        val delegateForViewType = getDelegateForViewType(viewType)
            ?: throw NullPointerException("No delegate found for viewType: $viewType")
        return delegateForViewType.onCreateViewHolder(parent)
    }

    fun onBindViewHolder(item: T, viewHolder: CustomThemeRecyclerViewHolder, theme: CustomTheme) {
        val viewType = viewHolder.itemViewType
        val delegateForViewType = getDelegateForViewType(viewType)
            ?: throw NullPointerException("No delegate found for viewType: $viewType")

        delegateForViewType.onBindViewHolder(item, viewHolder, theme)
    }

    private fun getDelegateForViewType(viewType: Int) =
        mAdapterDelegates.find { it.getItemViewType() == viewType }

    private fun getDelegateForItem(item: T) =
        mAdapterDelegates.find { it.isForViewType(item) }
}