package com.ak.passwordsaver.presentation.screens.passwords.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.ak.passwordsaver.R
import com.ak.passwordsaver.utils.PSUtils
import com.ak.passwordsaver.utils.bindView
import com.ak.passwordsaver.utils.extensions.drawTextInner
import com.ak.passwordsaver.utils.extensions.getColorCompat
import com.ak.passwordsaver.utils.extensions.setVisibility
import com.ak.passwordsaver.utils.extensions.setVisibilityInvisible

class PasswordsListItemViewHolder(
    itemView: View,
    private val onVisibilityPasswordAction: (passwordId: Long, newVisibilityState: Boolean) -> Unit,
    private val onShowPasswordItemActions: (passwordId: Long) -> Unit,
    private val onPasswordItemSingleClick: (passwordId: Long) -> Unit,
    private val onPasswordItemLongClick: (passwordId: Long) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        const val SECURE_PASSWORD_CONTENT_SYMBOL = "*"
    }

    private val mPasswordNameTextView: TextView by bindView(R.id.tv_password_name)
    private val mPasswordContentTextView: TextView by bindView(R.id.tv_password_content)
    private val mPasswordItemRoot: ViewGroup by bindView(R.id.cl_password_item_root)
    private val mPasswordAvatarImageView: ImageView by bindView(R.id.iv_password_avatar)
    private val mShowPasswordButton: Button by bindView(R.id.btn_password_visibility_action)
    private val mSelectedStateImageView: ImageView by bindView(R.id.iv_item_selected)
    private val mShowPasswordItemActionsButton: ImageView by bindView(R.id.iv_password_item_action)

    fun bindPasswordListItemView(passwordItemModel: PasswordItemModel) {
        mPasswordNameTextView.text = passwordItemModel.name
        mPasswordContentTextView.text = getPasswordContentText(passwordItemModel)
        mPasswordContentTextView.setVisibility(passwordItemModel.isPasswordContentNeeds)

        mShowPasswordButton.text = getVisibilityPasswordButtonText(
            passwordItemModel.isPasswordContentVisible
        )

        itemView.setOnClickListener {
            onPasswordItemSingleClick(passwordItemModel.passwordId)
        }

        itemView.setOnLongClickListener {
            onPasswordItemLongClick(passwordItemModel.passwordId)
            return@setOnLongClickListener true
        }

        initAdditionalItemClickListeners(passwordItemModel)

        val rootBackgroundResource = getRootItemBackground(passwordItemModel.isItemSelected)
        mPasswordItemRoot.setBackgroundResource(rootBackgroundResource)

        if (!passwordItemModel.isInActionModeState) {
            mSelectedStateImageView.setVisibilityInvisible(false)
        } else {
            mSelectedStateImageView.setVisibilityInvisible(passwordItemModel.isItemSelected)
        }

        mShowPasswordItemActionsButton.setVisibilityInvisible(!passwordItemModel.isInActionModeState)

        if (passwordItemModel.passwordAvatarBitmap != null) {
            mPasswordAvatarImageView.setImageBitmap(passwordItemModel.passwordAvatarBitmap)
        } else {
            val fillColor = itemView.context.getColorCompat(R.color.colorPrimary)
            val textColor = itemView.context.getColorCompat(R.color.colorWhite)
            val textSizeInPx = itemView.resources.getDimensionPixelSize(R.dimen.card_avatar_inner_text_size)
            val avatarSizeInPx = itemView.resources.getDimensionPixelSize(R.dimen.card_avatar_avatar_size)
            mPasswordAvatarImageView.drawTextInner(
                avatarSizeInPx,
                fillColor,
                textColor,
                textSizeInPx,
                PSUtils.getAbbreviationFormPasswordName(passwordItemModel.name)
            )
        }
    }

    private fun initAdditionalItemClickListeners(passwordItemModel: PasswordItemModel) {
        if (passwordItemModel.isInActionModeState) {
            for (i in 0..mPasswordItemRoot.childCount) {
                mPasswordItemRoot.getChildAt(i)?.apply {
                    isClickable = false
                    isFocusable = false
                }
            }
        } else {
            // init additional listeners
            val passwordId = passwordItemModel.passwordId
            mShowPasswordButton.setOnClickListener {
                onVisibilityPasswordAction(
                    passwordId,
                    !passwordItemModel.isPasswordContentVisible
                )
            }

            mShowPasswordItemActionsButton.setOnClickListener {
                onShowPasswordItemActions(passwordId)
            }
        }
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