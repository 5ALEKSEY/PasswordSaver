package com.ak.base.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface AdapterDelegate<T> {
    fun isForViewType(item: T): Boolean
    fun getItemViewType(): Int
    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    fun onBindViewHolder(item: T, viewHolder: RecyclerView.ViewHolder)
}