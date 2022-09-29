package com.ak.feature_tabaccounts_impl.screens.adapter

import android.graphics.drawable.AnimationDrawable
import android.view.View
import androidx.core.view.isVisible
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.CustomThemeDrawableBuilder
import com.ak.app_theme.theme.applier.CustomThemeApplier
import com.ak.base.constants.AppConstants
import com.ak.base.extensions.drawTextInner
import com.ak.base.extensions.setSafeClickListener
import com.ak.base.extensions.setVisibilityInvisible
import com.ak.base.ui.custom.popupmenu.PopupMenuHelper
import com.ak.base.ui.custom.popupmenu.PopupWindowMenuItem
import com.ak.base.ui.recycler.BasePopupMenuRecyclerViewHolder
import com.ak.base.utils.PSUtils
import com.ak.feature_tabaccounts_impl.R
import kotlinx.android.synthetic.main.accounts_item_layout.view.cvAccountItemContainer
import kotlinx.android.synthetic.main.accounts_item_layout.view.ivAccountAvatar
import kotlinx.android.synthetic.main.accounts_item_layout.view.ivItemPinned
import kotlinx.android.synthetic.main.accounts_item_layout.view.ivItemSelected
import kotlinx.android.synthetic.main.accounts_item_layout.view.tvAccountLogin
import kotlinx.android.synthetic.main.accounts_item_layout.view.tvAccountName
import kotlinx.android.synthetic.main.accounts_item_layout.view.tvAccountPassword
import kotlinx.android.synthetic.main.accounts_item_layout.view.vAccountItemRoot

class AccountsListItemViewHolder(
    itemView: View,
    private val listener: AccountListClickListener,
) : BasePopupMenuRecyclerViewHolder(itemView) {

    private var accountItemModel: AccountItemModel? = null

    override val popupMenuListener = object : PopupMenuHelper.Listener {
        override fun onItemClicked(menuItemId: Int) {
            val itemModel = accountItemModel ?: return
            when (menuItemId) {
                SELECT_ID -> listener.selectAccountItem(itemModel)
                COPY_LOGIN_ID -> listener.copyAccountItemLogin(itemModel)
                COPY_PASSWORD_ID -> listener.copyAccountItemPassword(itemModel)
                EDIT_ID -> listener.editAccountItem(itemModel)
                PIN_ID -> listener.pinAccount(itemModel)
                UNPIN_ID -> listener.unpinAccount(itemModel)
                DELETE_ID -> listener.deleteAccountItem(itemModel)
            }
        }

        override fun onShow() {
            accountItemModel?.let { listener.onShowPopupMenu(it) }
        }

        override fun onDismiss() {
            accountItemModel?.let { listener.onDismissPopupmenu(it) }
        }
    }

    override fun applyTheme(theme: CustomTheme) {
        super.applyTheme(theme)
        CustomThemeApplier.applyBackgroundTint(
            theme,
            itemView.cvAccountItemContainer,
            R.attr.themedSecondaryBackgroundColor,
        )
        CustomThemeApplier.applyTextColor(
            theme,
            R.attr.themedPrimaryTextColor,
            itemView.tvAccountName,
            itemView.tvAccountLogin,
            itemView.tvAccountPassword,
        )
        CustomThemeApplier.applyCompoundDrawablesTint(
            theme,
            R.attr.themedPrimaryColor,
            itemView.tvAccountLogin,
            itemView.tvAccountPassword,
        )
        applyPinItemBackground(theme)
        drawAccountAvatar(theme)
        drawAccountText(theme)
        drawPasswordContentText(theme)
    }

    fun bindAccountListItemView(accountItemModel: AccountItemModel, theme: CustomTheme) {
        this.accountItemModel = accountItemModel
        itemView.tvAccountName.text = accountItemModel.name

        itemView.tvAccountLogin.apply {
            drawAccountText(theme)
            maxLines = if (accountItemModel.isAccountContentVisible) {
                AppConstants.MAX_LINES_VISIBLE_CONTENT
            } else {
                AppConstants.MAX_LINES_INVISIBLE_CONTENT
            }
        }

        itemView.tvAccountPassword.apply {
            drawPasswordContentText(theme)
            maxLines = if (accountItemModel.isAccountContentVisible) {
                AppConstants.MAX_LINES_VISIBLE_CONTENT
            } else {
                AppConstants.MAX_LINES_INVISIBLE_CONTENT
            }
        }

        if (accountItemModel.isInActionModeState) {
            disablePopupMenuListener()
            itemView.setOnClickListener {
                listener.selectAccountItem(accountItemModel)
            }
            itemView.setOnLongClickListener {
                listener.selectAccountItem(accountItemModel)
                return@setOnLongClickListener true
            }
        } else {
            enablePopupMenuListener()
        }

        initAdditionalItemClickListeners(accountItemModel)

        setRootItemBackground(accountItemModel, itemView.vAccountItemRoot, theme)

        if (!accountItemModel.isInActionModeState) {
            itemView.ivItemSelected.setVisibilityInvisible(false)
        } else {
            itemView.ivItemSelected.apply {
                setVisibilityInvisible(accountItemModel.isItemSelected)
                setImageResource(theme.getDrawable(R.attr.themedSelectedItemDrawable))
            }
        }

        drawAccountAvatar(theme)

        itemView.ivItemPinned.isVisible = accountItemModel.isPinned
    }

    private fun drawAccountText(theme: CustomTheme) {
        val accountItemModel = accountItemModel ?: return

        itemView.tvAccountLogin.text = PSUtils.getHiddenContentText(
            itemView.context,
            accountItemModel.isAccountContentVisible,
            accountItemModel.login,
            theme.getDrawable(R.attr.themedHiddenContentDrawable),
        )
    }

    private fun drawPasswordContentText(theme: CustomTheme) {
        val accountItemModel = accountItemModel ?: return

        itemView.tvAccountPassword.text = PSUtils.getHiddenContentText(
            itemView.context,
            accountItemModel.isAccountContentVisible,
            accountItemModel.password,
            theme.getDrawable(R.attr.themedHiddenContentDrawable),
        )
    }

    private fun applyPinItemBackground(theme: CustomTheme) {
        itemView.ivItemPinned.background = CustomThemeDrawableBuilder(theme, itemView.context)
            .oval()
            .solidColorAttr(R.attr.themedAccentColor)
            .radius(itemView.resources.getDimensionPixelSize(R.dimen.pinned_account_icon_size).toFloat())
            .build()
    }

    private fun drawAccountAvatar(theme: CustomTheme) {
        val abbreviation = accountItemModel?.name?.let {
            PSUtils.getAbbreviationFormName(it)
        } ?: return

        val fillColor = theme.getColor(R.attr.staticColorTransparent)
        val textColor = theme.getColor(R.attr.themedPrimaryColor)
        val textSizeInPx = itemView.resources.getDimensionPixelSize(R.dimen.card_avatar_inner_text_size)
        val avatarSizeInPx = itemView.resources.getDimensionPixelSize(R.dimen.card_avatar_avatar_size)
        itemView.ivAccountAvatar.apply {
            drawTextInner(
                itemView.context,
                avatarSizeInPx,
                fillColor,
                textColor,
                textSizeInPx,
                abbreviation,
            )
            borderColor = theme.getColor(R.attr.themedPrimaryDarkColor)
        }
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

    private fun setRootItemBackground(accountItemModel: AccountItemModel, rootView: View, theme: CustomTheme) {
        val bgResId = when {
            accountItemModel.isLoadingModel -> theme.getDrawable(R.attr.themedSelectedItemBackgroundDrawable)
            else -> 0
        }
        rootView.setBackgroundResource(bgResId)

        (rootView.background as? AnimationDrawable)?.apply {
            setEnterFadeDuration(0)
            setExitFadeDuration(500)
            start()
        }
    }

    override fun generateMenuItems(): List<PopupWindowMenuItem> {
        val menuItems = mutableListOf<PopupWindowMenuItem>()

        menuItems.add(
            PopupWindowMenuItem(
                SELECT_ID,
                PopupWindowMenuItem.Image(drawableRes = R.drawable.ic_popup_menu_select),
                PopupWindowMenuItem.Title(contentRes = R.string.account_popup_menu_item_select),
            )
        )
        menuItems.add(
            PopupWindowMenuItem(
                COPY_LOGIN_ID,
                PopupWindowMenuItem.Image(drawableRes = R.drawable.ic_popup_menu_copy),
                PopupWindowMenuItem.Title(contentRes = R.string.account_popup_menu_item_copy_login),
            )
        )
        menuItems.add(
            PopupWindowMenuItem(
                COPY_PASSWORD_ID,
                PopupWindowMenuItem.Image(drawableRes = R.drawable.ic_popup_menu_copy),
                PopupWindowMenuItem.Title(contentRes = R.string.account_popup_menu_item_copy_password),
            )
        )
        menuItems.add(
            PopupWindowMenuItem(
                EDIT_ID,
                PopupWindowMenuItem.Image(drawableRes = R.drawable.ic_popup_menu_edit),
                PopupWindowMenuItem.Title(contentRes = R.string.account_popup_menu_item_edit),
            )
        )

        val pinMenuItem = if (accountItemModel?.isPinned == true) {
            PopupWindowMenuItem(
                UNPIN_ID,
                PopupWindowMenuItem.Image(drawableRes = R.drawable.ic_popup_menu_unpin),
                PopupWindowMenuItem.Title(contentRes = R.string.password_popup_menu_item_unpin),
            )
        } else {
            PopupWindowMenuItem(
                PIN_ID,
                PopupWindowMenuItem.Image(drawableRes = R.drawable.ic_popup_menu_pin),
                PopupWindowMenuItem.Title(contentRes = R.string.password_popup_menu_item_pin),
            )
        }
        menuItems.add(pinMenuItem)

        menuItems.add(
            PopupWindowMenuItem(
                DELETE_ID,
                PopupWindowMenuItem.Image(drawableRes = R.drawable.ic_popup_menu_delete),
                PopupWindowMenuItem.Title(contentRes = R.string.account_popup_menu_item_delete),
                PopupWindowMenuItem.ItemTint(
                    titleTintAttrRes = R.attr.themedErrorColor,
                    iconTintAttrRes = R.attr.themedErrorColor,
                )
            )
        )

        return menuItems
    }

    private companion object {
        const val ACCOUNT_SHOW_ACTION_CLICK_DELAY_IN_MILLIS = 700L

        const val SELECT_ID = 0
        const val COPY_LOGIN_ID = 1
        const val COPY_PASSWORD_ID = 2
        const val EDIT_ID = 3
        const val PIN_ID = 4
        const val UNPIN_ID = 5
        const val DELETE_ID = 6
    }
}