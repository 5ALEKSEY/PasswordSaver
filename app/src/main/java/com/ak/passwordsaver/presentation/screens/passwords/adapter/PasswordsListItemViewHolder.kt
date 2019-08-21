package com.ak.passwordsaver.presentation.screens.passwords.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ak.passwordsaver.R
import com.ak.passwordsaver.utils.bindView

class PasswordsListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        const val SECURE_PASSWORD_CONTENT_SYMBOL = "*"
    }

    private val mPasswordNameTextView: TextView by bindView(R.id.tv_password_name)
    private val mPasswordContentTextView: TextView by bindView(R.id.tv_password_content)
    private val mPasswordAvatarImageView: ImageView by bindView(R.id.iv_password_avatar)

    fun bindPasswordListItemView(passwordItemModel: PasswordItemModel) {
        mPasswordNameTextView.text = passwordItemModel.name
        mPasswordContentTextView.text = getPasswordContentText(passwordItemModel)
        mPasswordContentTextView.visibility = if (passwordItemModel.isPasswordContentNeeds) View.VISIBLE else View.GONE
    }

    private fun getPasswordContentText(passwordItemModel: PasswordItemModel) =
        if (passwordItemModel.isPasswordContentVisible) {
            passwordItemModel.password
        } else {
            val builder = StringBuilder(passwordItemModel.password.length)
            repeat(passwordItemModel.password.length) {
                builder.append(SECURE_PASSWORD_CONTENT_SYMBOL)
            }
            builder.toString()
        }
}