package com.ak.base.adapter

import android.view.ViewGroup
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.uicomponents.recyclerview.CustomThemeRecyclerViewHolder

interface AdapterDelegate<T> {
    fun isForViewType(item: T): Boolean
    fun getItemViewType(): Int
    fun onCreateViewHolder(parent: ViewGroup): CustomThemeRecyclerViewHolder
    fun onBindViewHolder(item: T, viewHolder: CustomThemeRecyclerViewHolder, theme: CustomTheme)
}