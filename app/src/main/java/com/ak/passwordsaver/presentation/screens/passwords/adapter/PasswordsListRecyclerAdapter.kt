package com.ak.passwordsaver.presentation.screens.passwords.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ak.passwordsaver.R

class PasswordsListRecyclerAdapter(private val onShowPasswordAction: (passwordId: Long) -> Unit) :
    RecyclerView.Adapter<PasswordsListItemViewHolder>() {

    private var mItemsList = arrayListOf<PasswordItemModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasswordsListItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.passwords_item_layout, parent, false)
        return PasswordsListItemViewHolder(view, onShowPasswordAction)
    }

    override fun getItemCount() = mItemsList.size

    override fun onBindViewHolder(viewHolder: PasswordsListItemViewHolder, position: Int) {
        viewHolder.bindPasswordListItemView(mItemsList[position])
    }

    fun insertData(passwordModels: List<PasswordItemModel>) {
        mItemsList.addAll(passwordModels)
        notifyDataSetChanged()
    }

    fun openPasswordForPasswordItemId(passwordId: Long) {
        val index = mItemsList.indexOf(PasswordItemModel.getSearchingTempModel(passwordId))
        mItemsList.find { passwordItemModel -> passwordItemModel.passwordId == passwordId }
            ?.let {
                it.isPasswordContentVisible = true
                changePasswordItem(it, index)
            }
    }

    fun changePasswordItem(newPasswordItemModel: PasswordItemModel, position: Int) {
        if (position >= 0) {
            mItemsList[position] = newPasswordItemModel
            notifyItemChanged(position)
        }
    }
}