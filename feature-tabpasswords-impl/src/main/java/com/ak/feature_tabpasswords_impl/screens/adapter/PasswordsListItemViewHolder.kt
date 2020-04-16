package com.ak.feature_tabpasswords_impl.screens.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ak.base.extensions.drawTextInner
import com.ak.base.extensions.getColorCompat
import com.ak.base.extensions.setSafeClickListener
import com.ak.base.extensions.setVisibility
import com.ak.base.extensions.setVisibilityInvisible
import com.ak.base.utils.PSUtils
import com.ak.feature_tabpasswords_impl.R
import kotlinx.android.synthetic.main.passwords_item_layout.view.*

class PasswordsListItemViewHolder(
    itemView: View,
    private val onVisibilityPasswordAction: (passwordId: Long, newVisibilityState: Boolean) -> Unit,
    private val onShowPasswordItemActions: (passwordId: Long) -> Unit,
    private val onPasswordItemSingleClick: (passwordId: Long) -> Unit,
    private val onPasswordItemLongClick: (passwordId: Long) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        const val SECURE_PASSWORD_CONTENT_SYMBOL = "*"
        const val PASSWORD_SHOW_ACTION_CLICK_DELAY_IN_MILLIS = 700L
    }

    fun bindPasswordListItemView(passwordItemModel: PasswordItemModel) {
        itemView.tvPasswordName.text = passwordItemModel.name
        itemView.tvPasswordContent.text = getPasswordContentText(passwordItemModel)
        itemView.tvPasswordContent.setVisibility(passwordItemModel.isPasswordContentNeeds)

        itemView.btnPasswordVisibilityAction.text = getVisibilityPasswordButtonText(
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
        itemView.vPasswordItemRoot.setBackgroundResource(rootBackgroundResource)

        if (!passwordItemModel.isInActionModeState) {
            itemView.ivItemSelected.setVisibilityInvisible(false)
        } else {
            itemView.ivItemSelected.setVisibilityInvisible(passwordItemModel.isItemSelected)
        }

        itemView.ivPasswordItemAction.setVisibilityInvisible(!passwordItemModel.isInActionModeState)

        if (passwordItemModel.passwordAvatarBitmap != null) {
            itemView.ivPasswordAvatar.setImageBitmap(passwordItemModel.passwordAvatarBitmap)
        } else {
            val fillColor = itemView.context.getColorCompat(R.color.colorPrimary)
            val textColor = itemView.context.getColorCompat(R.color.colorWhite)
            val textSizeInPx =
                itemView.resources.getDimensionPixelSize(R.dimen.card_avatar_inner_text_size)
            val avatarSizeInPx =
                itemView.resources.getDimensionPixelSize(R.dimen.card_avatar_avatar_size)
            itemView.ivPasswordAvatar.drawTextInner(
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
            for (i in 0..itemView.vPasswordItemRoot.childCount) {
                itemView.vPasswordItemRoot.getChildAt(i)?.apply {
                    isClickable = false
                    isFocusable = false
                }
            }
        } else {
            // init additional listeners
            val passwordId = passwordItemModel.passwordId
            itemView.btnPasswordVisibilityAction.setSafeClickListener(
                PASSWORD_SHOW_ACTION_CLICK_DELAY_IN_MILLIS
            ) {
                onVisibilityPasswordAction(
                    passwordId,
                    !passwordItemModel.isPasswordContentVisible
                )
            }

            itemView.ivPasswordItemAction.setSafeClickListener {
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