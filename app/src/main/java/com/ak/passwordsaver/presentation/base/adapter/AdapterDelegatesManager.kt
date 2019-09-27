package com.ak.passwordsaver.presentation.base.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

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

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val delegateForViewType = getDelegateForViewType(viewType)
            ?: throw NullPointerException("No delegate found for viewType: $viewType")
        return delegateForViewType.onCreateViewHolder(parent)
    }

    fun onBindViewHolder(item: T, viewHolder: RecyclerView.ViewHolder) {
        val viewType = viewHolder.itemViewType
        val delegateForViewType = getDelegateForViewType(viewType)
            ?: throw NullPointerException("No delegate found for viewType: $viewType")

        delegateForViewType.onBindViewHolder(item, viewHolder)
    }

    private fun getDelegateForViewType(viewType: Int) =
        mAdapterDelegates.find { it.getItemViewType() == viewType }

    private fun getDelegateForItem(item: T) =
        mAdapterDelegates.find { it.isForViewType(item) }
}