package com.ak.passwordsaver.presentation.screens.passwords.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ak.passwordsaver.R

class PasswordsListRecyclerAdapter : RecyclerView.Adapter<PasswordsListItemViewHolder>() {

    private var mItemsList = arrayListOf<PasswordItemModel>()

    public fun insertData(passwordModels: List<PasswordItemModel>) {
        mItemsList.addAll(passwordModels)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasswordsListItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.passwords_item_layout, parent, false)
        return PasswordsListItemViewHolder(view)
    }

    override fun getItemCount() = mItemsList.size

    override fun onBindViewHolder(viewHolder: PasswordsListItemViewHolder, position: Int) {
        viewHolder.bindPasswordListItemView(mItemsList[position])
    }
}