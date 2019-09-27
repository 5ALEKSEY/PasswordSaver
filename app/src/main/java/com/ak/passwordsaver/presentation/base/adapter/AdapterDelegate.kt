package com.ak.passwordsaver.presentation.base.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

interface AdapterDelegate<T> {
    fun isForViewType(item: T): Boolean
    fun getItemViewType(): Int
    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    fun onBindViewHolder(item: T, viewHolder: RecyclerView.ViewHolder)
}