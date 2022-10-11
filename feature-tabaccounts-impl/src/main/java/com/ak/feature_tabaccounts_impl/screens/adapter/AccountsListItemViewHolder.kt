package com.ak.feature_tabaccounts_impl.screens.adapter

import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.view.isVisible
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.CustomThemeDrawableBuilder
import com.ak.app_theme.theme.applier.CustomThemeApplier
import com.ak.base.constants.AppConstants
import com.ak.base.extensions.drawTextInner
import com.ak.base.extensions.pxToDp
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
        applySelectedItemIconBackground(theme)
        applyPinItemBackground(theme)
        drawAccountAvatar(theme)
        drawAccountText(theme)
        drawPasswordContentText(theme)
    }

    fun bindAccountListItemView(accountItemModel: AccountItemModel, theme: CustomTheme) {
        this.accountItemModel = accountItemModel
        itemView.tvAccountName.text = accountItemModel.name

        itemView.tvAccountLogin.apply {
            maxLines = if (accountItemModel.isAccountContentVisible) {
                AppConstants.MAX_LINES_VISIBLE_CONTENT
            } else {
                AppConstants.MAX_LINES_INVISIBLE_CONTENT
            }
            drawAccountText(theme)
        }

        itemView.tvAccountPassword.apply {
            maxLines = if (accountItemModel.isAccountContentVisible) {
                AppConstants.MAX_LINES_VISIBLE_CONTENT
            } else {
                AppConstants.MAX_LINES_INVISIBLE_CONTENT
            }
            drawPasswordContentText(theme)
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
            }
        }

        drawAccountAvatar(theme)

        itemView.ivItemPinned.isVisible = accountItemModel.isPinned
    }

    private fun drawAccountText(theme: CustomTheme) {
        val accountItemModel = accountItemModel ?: return

        itemView.tvAccountLogin.text = PSUtils.getHiddenContentTextTemp(
            accountItemModel.isAccountContentVisible,
            accountItemModel.login,
            createHiddenItemDrawable(theme),
        )
    }

    private fun drawPasswordContentText(theme: CustomTheme) {
        val accountItemModel = accountItemModel ?: return

        itemView.tvAccountPassword.text = PSUtils.getHiddenContentTextTemp(
            accountItemModel.isAccountContentVisible,
            accountItemModel.password,
            createHiddenItemDrawable(theme),
        )
    }

    private fun applyPinItemBackground(theme: CustomTheme) {
        itemView.ivItemPinned.background = CustomThemeDrawableBuilder(theme, itemView.context)
            .oval()
            .solidColorAttr(R.attr.themedAccentColor)
            .radius(itemView.resources.getDimensionPixelSize(R.dimen.pinned_account_icon_size).toFloat())
            .build()
    }

    private fun applySelectedItemIconBackground(theme: CustomTheme) {
        itemView.ivItemSelected.background = CustomThemeDrawableBuilder(theme, itemView.context)
            .oval()
            .solidColorAttr(R.attr.themedAccentColor)
            .radius(itemView.resources.getDimensionPixelSize(R.dimen.pinned_account_selected_item_icon_size).toFloat())
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
        if (accountItemModel.isLoadingModel) {
            rootView.background = createLoadingModelDrawable(theme)
        } else {
            rootView.setBackgroundResource(0)
        }

        (rootView.background as? AnimationDrawable)?.start()
    }

    // TODO: Move to utils or smth like that
    private fun createLoadingModelDrawable(theme: CustomTheme): Drawable {
        val startGradientDrawable = CustomThemeDrawableBuilder(theme, itemView.context)
            .rectangle()
            .gradientOrientation(GradientDrawable.Orientation.RIGHT_LEFT)
            .linearGradientType()
            .gradientColorsAttr(
                R.attr.themedSelectedGradientStartColor,
                R.attr.themedSelectedGradientCenterColor,
                R.attr.themedSelectedGradientEndColor,
            )
            .build()

        val centerGradientDrawable = CustomThemeDrawableBuilder(theme, itemView.context)
            .rectangle()
            .gradientOrientation(GradientDrawable.Orientation.RIGHT_LEFT)
            .linearGradientType()
            .gradientColorsAttr(
                R.attr.themedSelectedGradientEndColor,
                R.attr.themedSelectedGradientStartColor,
                R.attr.themedSelectedGradientCenterColor,
            )
            .build()

        val endGradientDrawable = CustomThemeDrawableBuilder(theme, itemView.context)
            .rectangle()
            .gradientOrientation(GradientDrawable.Orientation.RIGHT_LEFT)
            .linearGradientType()
            .gradientColorsAttr(
                R.attr.themedSelectedGradientCenterColor,
                R.attr.themedSelectedGradientEndColor,
                R.attr.themedSelectedGradientStartColor,
            )
            .build()

        return AnimationDrawable().apply {
            addFrame(startGradientDrawable, 500)
            addFrame(centerGradientDrawable, 500)
            addFrame(endGradientDrawable, 500)

            setEnterFadeDuration(0)
            setExitFadeDuration(500)
        }
    }

    private fun createHiddenItemDrawable(theme: CustomTheme): Drawable {
        val hiddenItemRadiusPx = itemView.resources.getDimensionPixelSize(R.dimen.account_hidden_item_radius)
        val hiddenItemSizePx = itemView.resources.getDimensionPixelSize(R.dimen.account_hidden_item_size)
        val hiddenItemPaddingPx = itemView.resources.getDimensionPixelSize(R.dimen.account_hidden_item_padding)

        val hiddenContentOvalDrawable = CustomThemeDrawableBuilder(theme, itemView.context)
            .oval()
            .radius(hiddenItemRadiusPx.toFloat())
            .solidColorAttr(R.attr.themedPrimaryTextColor)
            .build()

        return CustomThemeDrawableBuilder(theme, itemView.context)
            .oval()
            .size(hiddenItemSizePx, hiddenItemSizePx)
            .addDrawableWithInsetsDp(
                drawable = hiddenContentOvalDrawable,
                leftDp = 0F,
                topDp = hiddenItemPaddingPx.pxToDp(itemView.context),
                rightDp = hiddenItemPaddingPx.pxToDp(itemView.context),
                bottomDp = 0F,
            )
            .build().apply {
                setBounds(0, 0, hiddenItemSizePx, hiddenItemSizePx)
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