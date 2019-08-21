package com.ak.passwordsaver.presentation.screens.passwords.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ak.passwordsaver.R
import com.ak.passwordsaver.utils.bindView

class PasswordsListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val mPasswordNameTextView: TextView by bindView(R.id.tv_password_name)
    private val mPasswordContentTextView: TextView by bindView(R.id.tv_password_content)
    private val mPasswordAvatarImageView: ImageView by bindView(R.id.iv_password_avatar)

    fun bindPasswordListItemView(passwordItemModel: PasswordItemModel) {
        mPasswordNameTextView.text = passwordItemModel.name
        mPasswordContentTextView.text = passwordItemModel.password
        mPasswordContentTextView.visibility = if (passwordItemModel.isPasswordContentNeeds) View.VISIBLE else View.GONE
    }
}