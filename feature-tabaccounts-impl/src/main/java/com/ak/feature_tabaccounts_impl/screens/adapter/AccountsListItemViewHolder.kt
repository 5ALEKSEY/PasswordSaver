package com.ak.feature_tabaccounts_impl.screens.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ak.base.constants.AppConstants
import com.ak.base.extensions.drawTextInner
import com.ak.base.extensions.getColorCompat
import com.ak.base.extensions.setSafeClickListener
import com.ak.base.extensions.setVisibilityInvisible
import com.ak.base.utils.PSUtils
import com.ak.feature_tabaccounts_impl.R
import kotlinx.android.synthetic.main.accounts_item_layout.view.*

class AccountsListItemViewHolder(
    itemView: View,
    private val onVisibilityAccountAction: (accountId: Long, newVisibilityState: Boolean) -> Unit,
    private val onShowAccountItemActions: (accountId: Long) -> Unit,
    private val onAccountItemSingleClick: (accountId: Long) -> Unit,
    private val onAccountItemLongClick: (accountId: Long) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        const val ACCOUNT_SHOW_ACTION_CLICK_DELAY_IN_MILLIS = 700L
    }

    fun bindAccountListItemView(accountItemModel: AccountItemModel) {
        itemView.tvAccountName.text = accountItemModel.name

        itemView.tvAccountLogin.apply {
            text = PSUtils.getHidedContentText(
                itemView.context,
                accountItemModel.isAccountContentVisible,
                accountItemModel.login
            )
            maxLines = if (accountItemModel.isAccountContentVisible) {
                AppConstants.MAX_LINES_VISIBLE_CONTENT
            } else {
                AppConstants.MAX_LINES_INVISIBLE_CONTENT
            }
        }

        itemView.tvAccountPassword.apply {
            text = PSUtils.getHidedContentText(
                itemView.context,
                accountItemModel.isAccountContentVisible,
                accountItemModel.password
            )
            maxLines = if (accountItemModel.isAccountContentVisible) {
                AppConstants.MAX_LINES_VISIBLE_CONTENT
            } else {
                AppConstants.MAX_LINES_INVISIBLE_CONTENT
            }
        }

        itemView.setOnClickListener {
            onAccountItemSingleClick(accountItemModel.accountId)
        }

        itemView.setOnLongClickListener {
            onAccountItemLongClick(accountItemModel.accountId)
            return@setOnLongClickListener true
        }

        initAdditionalItemClickListeners(accountItemModel)

        val rootBackgroundResource = getRootItemBackground(accountItemModel.isItemSelected)
        itemView.vAccountItemRoot.setBackgroundResource(rootBackgroundResource)

        if (!accountItemModel.isInActionModeState) {
            itemView.ivItemSelected.setVisibilityInvisible(false)
        } else {
            itemView.ivItemSelected.setVisibilityInvisible(accountItemModel.isItemSelected)
        }

        itemView.ivAccountItemAction.setVisibilityInvisible(!accountItemModel.isInActionModeState)

        val fillColor = itemView.context.getColorCompat(R.color.staticColorTransparent)
        val textColor = itemView.context.getColorCompat(R.color.colorPrimary)
        val textSizeInPx = itemView.resources.getDimensionPixelSize(R.dimen.card_avatar_inner_text_size)
        val avatarSizeInPx = itemView.resources.getDimensionPixelSize(R.dimen.card_avatar_avatar_size)
        itemView.ivAccountAvatar.drawTextInner(
            itemView.context,
            avatarSizeInPx,
            fillColor,
            textColor,
            textSizeInPx,
            PSUtils.getAbbreviationFormName(accountItemModel.name)
        )
    }

    private fun initAdditionalItemClickListeners(accountItemModel: AccountItemModel) {
        if (accountItemModel.isInActionModeState) {
            for (i in 0..itemView.vAccountItemRoot.childCount) {
                itemView.vAccountItemRoot.getChildAt(i)?.apply {
                    isClickable = false
                    isFocusable = false
                }
            }
        } else {
            // init additional listeners
            itemView.setSafeClickListener(
                ACCOUNT_SHOW_ACTION_CLICK_DELAY_IN_MILLIS
            ) {
                onVisibilityAccountAction(
                    accountItemModel.accountId,
                    !accountItemModel.isAccountContentVisible
                )
            }

            itemView.ivAccountItemAction.setSafeClickListener {
                onShowAccountItemActions(accountItemModel.accountId)
            }
        }
    }

    private fun getRootItemBackground(isItemSelected: Boolean) =
        if (isItemSelected) {
            R.drawable.bg_selected_account_item
        } else {
            0
        }
}