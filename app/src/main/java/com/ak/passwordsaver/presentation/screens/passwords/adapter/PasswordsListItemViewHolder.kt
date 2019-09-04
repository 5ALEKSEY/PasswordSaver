package com.ak.passwordsaver.presentation.screens.passwords.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.ak.passwordsaver.R
import com.ak.passwordsaver.utils.bindView

class PasswordsListItemViewHolder(
    itemView: View,
    private val onVisibilityPasswordAction: (passwordId: Long, newVisibilityState: Boolean) -> Unit,
    private val onPasswordItemSingleClick: (passwordId: Long) -> Unit,
    private val onPasswordItemLongClick: (passwordId: Long) -> Unit
) :
    RecyclerView.ViewHolder(itemView) {

    companion object {
        const val SECURE_PASSWORD_CONTENT_SYMBOL = "*"
    }

    private val mPasswordNameTextView: TextView by bindView(R.id.tv_password_name)
    private val mPasswordContentTextView: TextView by bindView(R.id.tv_password_content)
    private val mPasswordItemRoot: View by bindView(R.id.cl_password_item_root)
    private val mPasswordAvatarImageView: ImageView by bindView(R.id.iv_password_avatar)
    private val mVisibilityPasswordButton: Button by bindView(R.id.btn_password_visibility_action)

    fun bindPasswordListItemView(passwordItemModel: PasswordItemModel) {
        mPasswordNameTextView.text = passwordItemModel.name
        mPasswordContentTextView.text = getPasswordContentText(passwordItemModel)
        mPasswordContentTextView.visibility = if (passwordItemModel.isPasswordContentNeeds) View.VISIBLE else View.GONE

        mVisibilityPasswordButton.text = getVisibilityPasswordButtonText(passwordItemModel.isPasswordContentVisible)
        mVisibilityPasswordButton.setOnClickListener {
            onVisibilityPasswordAction.invoke(passwordItemModel.passwordId, !passwordItemModel.isPasswordContentVisible)
        }

        itemView.setOnClickListener {
            onPasswordItemSingleClick.invoke(passwordItemModel.passwordId)
        }

        itemView.setOnLongClickListener {
            onPasswordItemLongClick.invoke(passwordItemModel.passwordId)
            return@setOnLongClickListener true
        }

        val rootBackgroundResource = getRootItemBackground(passwordItemModel.isItemSelected)
        mPasswordItemRoot.setBackgroundResource(rootBackgroundResource)
    }

    private fun getVisibilityPasswordButtonText(isPasswordContentVisible: Boolean) =
        if (isPasswordContentVisible) "Hide" else "Show"

    private fun getRootItemBackground(isItemSelected: Boolean) =
        if (isItemSelected) {
            R.drawable.bg_selected_password_item
        } else {
            0
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