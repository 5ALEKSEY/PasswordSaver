package com.ak.feature_tabpasswords_impl.screens.adapter

import android.graphics.drawable.AnimationDrawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ak.base.constants.AppConstants
import com.ak.base.extensions.drawTextInner
import com.ak.base.extensions.getColorCompat
import com.ak.base.extensions.setSafeClickListener
import com.ak.base.extensions.setVisibilityInvisible
import com.ak.base.utils.PSUtils
import com.ak.feature_tabpasswords_impl.R
import kotlinx.android.synthetic.main.passwords_item_layout.view.*

class PasswordsListItemViewHolder(
    itemView: View,
    private val onVisibilityPasswordAction: (passwordId: Long, newVisibilityState: Boolean) -> Unit,
    private val onShowPasswordItemActions: (passwordId: Long, isContentVisible: Boolean) -> Unit,
    private val onPasswordItemSingleClick: (passwordId: Long) -> Unit,
    private val onPasswordItemLongClick: (passwordId: Long) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        const val PASSWORD_SHOW_ACTION_CLICK_DELAY_IN_MILLIS = 700L
    }

    fun bindPasswordListItemView(passwordItemModel: PasswordItemModel) {
        itemView.tvPasswordName.text = passwordItemModel.name

        itemView.tvPasswordContent.apply {
            text = PSUtils.getHidedContentText(
                itemView.context,
                passwordItemModel.isPasswordContentVisible,
                passwordItemModel.password
            )
            maxLines = if (passwordItemModel.isPasswordContentVisible) {
                AppConstants.MAX_LINES_VISIBLE_CONTENT
            } else {
                AppConstants.MAX_LINES_INVISIBLE_CONTENT
            }
        }

        itemView.setOnClickListener {
            onPasswordItemSingleClick(passwordItemModel.passwordId)
        }

        itemView.setOnLongClickListener {
            onPasswordItemLongClick(passwordItemModel.passwordId)
            return@setOnLongClickListener true
        }

        initAdditionalItemClickListeners(passwordItemModel)

        setRootItemBackground(passwordItemModel, itemView.vPasswordItemRoot)

        if (!passwordItemModel.isInActionModeState) {
            itemView.ivItemSelected.setVisibilityInvisible(false)
        } else {
            itemView.ivItemSelected.setVisibilityInvisible(passwordItemModel.isItemSelected)
        }

        itemView.ivPasswordItemAction.setVisibilityInvisible(!passwordItemModel.isInActionModeState)

        if (passwordItemModel.passwordAvatarBitmap != null) {
            itemView.ivPasswordAvatar.setImageBitmap(passwordItemModel.passwordAvatarBitmap)
        } else {
            val fillColor = itemView.context.getColorCompat(R.color.staticColorTransparent)
            val textColor = itemView.context.getColorCompat(R.color.colorPrimary)
            val textSizeInPx = itemView.resources.getDimensionPixelSize(
                R.dimen.card_avatar_inner_text_size
            )
            val avatarSizeInPx = itemView.resources.getDimensionPixelSize(
                R.dimen.card_avatar_avatar_size
            )
            itemView.ivPasswordAvatar.drawTextInner(
                itemView.context,
                avatarSizeInPx,
                fillColor,
                textColor,
                textSizeInPx,
                PSUtils.getAbbreviationFormName(passwordItemModel.name)
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
            itemView.setSafeClickListener(
                PASSWORD_SHOW_ACTION_CLICK_DELAY_IN_MILLIS
            ) {
                onVisibilityPasswordAction(
                    passwordItemModel.passwordId,
                    !passwordItemModel.isPasswordContentVisible
                )
            }

            itemView.ivPasswordItemAction.setSafeClickListener {
                onShowPasswordItemActions(passwordItemModel.passwordId, passwordItemModel.isPasswordContentVisible)
            }
        }
    }

    private fun setRootItemBackground(passwordItemModel: PasswordItemModel, rootView: View) {
        val bgResId = when {
            passwordItemModel.isItemSelected -> R.drawable.bg_selected_password_item
            passwordItemModel.isLoadingModel -> R.drawable.loading_animation
            else -> 0
        }
        rootView.setBackgroundResource(bgResId)

        (rootView.background as? AnimationDrawable)?.apply {
            setEnterFadeDuration(0)
            setExitFadeDuration(400)
            start()
        }
    }
}