package com.ak.feature_tabaccounts_impl.screens.adapter

import android.graphics.drawable.AnimationDrawable
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
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
    private val listener: AccountListClickListener
) : RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

    private var accountItemModel: AccountItemModel? = null

    companion object {
        const val ACCOUNT_SHOW_ACTION_CLICK_DELAY_IN_MILLIS = 700L

        const val CONTEXT_MENU_SELECT_ID = 1
        const val CONTEXT_MENU_COPY_LOGIN_ID = 2
        const val CONTEXT_MENU_COPY_PASSWORD_ID = 3
        const val CONTEXT_MENU_EDIT_ID = 4
        const val CONTEXT_MENU_DELETE_ID = 5
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            CONTEXT_MENU_SELECT_ID -> {
                accountItemModel?.let {
                    listener.selectAccountItem(it)
                    true
                } ?: false
            }
            CONTEXT_MENU_COPY_LOGIN_ID -> {
                accountItemModel?.let {
                    listener.copyAccountItemLogin(it)
                    true
                } ?: false
            }
            CONTEXT_MENU_COPY_PASSWORD_ID -> {
                accountItemModel?.let {
                    listener.copyAccountItemPassword(it)
                    true
                } ?: false
            }
            CONTEXT_MENU_EDIT_ID -> {
                accountItemModel?.let {
                    listener.editAccountItem(it)
                    true
                } ?: false
            }
            CONTEXT_MENU_DELETE_ID -> {
                accountItemModel?.let {
                    listener.deleteAccountItem(it)
                    true
                } ?: false
            }
            else -> false
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        accountItemModel?.let { listener.onCreateContextMenuForAccountItem(it) }
        menu?.apply {
            addContextMenuItem(this, CONTEXT_MENU_SELECT_ID, "Select")
            addContextMenuItem(this, CONTEXT_MENU_COPY_LOGIN_ID, "Copy login")
            addContextMenuItem(this, CONTEXT_MENU_COPY_PASSWORD_ID, "Copy password")
            addContextMenuItem(this, CONTEXT_MENU_EDIT_ID, "Edit")
            addContextMenuItem(this, CONTEXT_MENU_DELETE_ID, "Delete")
        }
    }

    private fun addContextMenuItem(menu: ContextMenu, itemId: Int, itemTitle: String) {
        menu.add(Menu.NONE, itemId, Menu.NONE, itemTitle).setOnMenuItemClickListener(this)
    }

    fun bindAccountListItemView(accountItemModel: AccountItemModel) {
        this.accountItemModel = accountItemModel
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

        if (accountItemModel.isInActionModeState) {
            itemView.setOnClickListener {
                listener.selectAccountItem(accountItemModel)
            }
        } else {
            itemView.setOnClickListener(null)
        }

        itemView.setOnCreateContextMenuListener(this)

        if (accountItemModel.isInActionModeState) {
            itemView.setOnLongClickListener {
                listener.selectAccountItem(accountItemModel)
                return@setOnLongClickListener true
            }
        } else {
            itemView.setOnLongClickListener(null)
        }

        initAdditionalItemClickListeners(accountItemModel)

        setRootItemBackground(accountItemModel, itemView.vAccountItemRoot)

        if (!accountItemModel.isInActionModeState) {
            itemView.ivItemSelected.setVisibilityInvisible(false)
        } else {
            itemView.ivItemSelected.setVisibilityInvisible(accountItemModel.isItemSelected)
        }

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
                listener.showAccountItemContent(accountItemModel)
            }
        }
    }

    private fun setRootItemBackground(accountItemModel: AccountItemModel, rootView: View) {
        val bgResId = when {
            accountItemModel.isLoadingModel -> R.drawable.loading_animation
            else -> 0
        }
        rootView.setBackgroundResource(bgResId)

        (rootView.background as? AnimationDrawable)?.apply {
            setEnterFadeDuration(0)
            setExitFadeDuration(500)
            start()
        }
    }
}