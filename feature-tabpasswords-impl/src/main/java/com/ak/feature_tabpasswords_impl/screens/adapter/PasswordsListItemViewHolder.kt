package com.ak.feature_tabpasswords_impl.screens.adapter

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
import com.ak.feature_tabpasswords_impl.R
import kotlinx.android.synthetic.main.passwords_item_layout.view.cvPasswordItemContainer
import kotlinx.android.synthetic.main.passwords_item_layout.view.ivItemPinned
import kotlinx.android.synthetic.main.passwords_item_layout.view.ivItemSelected
import kotlinx.android.synthetic.main.passwords_item_layout.view.ivPasswordAvatar
import kotlinx.android.synthetic.main.passwords_item_layout.view.tvPasswordContent
import kotlinx.android.synthetic.main.passwords_item_layout.view.tvPasswordName
import kotlinx.android.synthetic.main.passwords_item_layout.view.vPasswordItemRoot

class PasswordsListItemViewHolder(
    itemView: View,
    private val listener: PasswordsListClickListener,
) : BasePopupMenuRecyclerViewHolder(itemView) {

    override val popupMenuListener = object : PopupMenuHelper.Listener {
        override fun onItemClicked(menuItemId: Int) {
            val itemModel = passwordItemModel ?: return
            when (menuItemId) {
                SELECT_ID -> listener.selectPasswordItem(itemModel)
                COPY_ID -> listener.copyPasswordItemContent(itemModel)
                EDIT_ID -> listener.editPasswordItem(itemModel)
                PIN_ID -> listener.pinPasswordItem(itemModel)
                UNPIN_ID -> listener.unpinPasswordItem(itemModel)
                DELETE_ID -> listener.deletePasswordItem(itemModel)
            }
        }

        override fun onShow() {
            passwordItemModel?.let { listener.onShowPopupMenu(it) }
        }

        override fun onDismiss() {
            passwordItemModel?.let { listener.onDismissPopupmenu(it) }
        }
    }

    private var passwordItemModel: PasswordItemModel? = null

    override fun applyTheme(theme: CustomTheme) {
        super.applyTheme(theme)
        CustomThemeApplier.applyBackgroundTint(
            theme,
            itemView.cvPasswordItemContainer,
            R.attr.themedSecondaryBackgroundColor,
        )
        CustomThemeApplier.applyTextColor(
            theme,
            R.attr.themedPrimaryTextColor,
            itemView.tvPasswordName,
            itemView.tvPasswordContent,
        )
        applyPinItemBackground(theme)
        drawEmptyPasswordAvatarIfNeeds(theme)
        drawPasswordContentText(theme)
    }

    fun bindPasswordListItemView(passwordItemModel: PasswordItemModel, theme: CustomTheme) {
        this.passwordItemModel = passwordItemModel

        itemView.tvPasswordName.text = passwordItemModel.name

        itemView.tvPasswordContent.apply {
            drawPasswordContentText(theme)
            maxLines = if (passwordItemModel.isPasswordContentVisible) {
                AppConstants.MAX_LINES_VISIBLE_CONTENT
            } else {
                AppConstants.MAX_LINES_INVISIBLE_CONTENT
            }
        }

        if (passwordItemModel.isInActionModeState) {
            disablePopupMenuListener()
            itemView.setOnClickListener {
                listener.selectPasswordItem(passwordItemModel)
            }
            itemView.setOnLongClickListener {
                listener.selectPasswordItem(passwordItemModel)
                return@setOnLongClickListener true
            }
        } else {
            enablePopupMenuListener()
        }

        initAdditionalItemClickListeners(passwordItemModel)

        setRootItemBackground(passwordItemModel, itemView.vPasswordItemRoot, theme)

        if (!passwordItemModel.isInActionModeState) {
            itemView.ivItemSelected.setVisibilityInvisible(false)
        } else {
            itemView.ivItemSelected.apply {
                setVisibilityInvisible(passwordItemModel.isItemSelected)
                setImageResource(theme.getDrawable(R.attr.themedSelectedItemDrawable))
            }
        }

        if (passwordItemModel.passwordAvatarBitmap != null) {
            itemView.ivPasswordAvatar.setImageBitmap(passwordItemModel.passwordAvatarBitmap)
        }
        drawEmptyPasswordAvatarIfNeeds(theme)

        itemView.ivItemPinned.isVisible = passwordItemModel.isPinned
    }

    private fun drawPasswordContentText(theme: CustomTheme) {
        val passwordItemModel = passwordItemModel ?: return

        itemView.tvPasswordContent.text = PSUtils.getHiddenContentText(
            itemView.context,
            passwordItemModel.isPasswordContentVisible,
            passwordItemModel.password,
            theme.getDrawable(R.attr.themedHiddenContentDrawable),
        )
    }

    private fun applyPinItemBackground(theme: CustomTheme) {
        itemView.ivItemPinned.background = CustomThemeDrawableBuilder(theme, itemView.context)
            .oval()
            .solidColorAttr(R.attr.themedAccentColor)
            .radius(itemView.resources.getDimensionPixelSize(R.dimen.pinned_password_icon_size).toFloat())
            .build()
    }

    private fun drawEmptyPasswordAvatarIfNeeds(theme: CustomTheme) {
        val abbreviation = passwordItemModel?.name?.let {
            PSUtils.getAbbreviationFormName(it)
        } ?: return

        val fillColor = theme.getColor(R.attr.staticColorTransparent)
        val textColor = theme.getColor(R.attr.themedPrimaryColor)
        val textSizeInPx = itemView.resources.getDimensionPixelSize(
            R.dimen.card_avatar_inner_text_size
        )
        val avatarSizeInPx = itemView.resources.getDimensionPixelSize(
            R.dimen.card_avatar_avatar_size
        )
        itemView.ivPasswordAvatar.apply {
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
                listener.showPasswordItemContent(passwordItemModel)
            }
        }
    }

    private fun setRootItemBackground(passwordItemModel: PasswordItemModel, rootView: View, theme: CustomTheme) {
        val bgResId = when {
            passwordItemModel.isLoadingModel -> theme.getDrawable(R.attr.themedSelectedItemBackgroundDrawable)
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
                PopupWindowMenuItem.Title(contentRes = R.string.password_popup_menu_item_select),
            ),
        )
        menuItems.add(
            PopupWindowMenuItem(
                COPY_ID,
                PopupWindowMenuItem.Image(drawableRes = R.drawable.ic_popup_menu_copy),
                PopupWindowMenuItem.Title(contentRes = R.string.password_popup_menu_item_copy),
            )
        )
        menuItems.add(
            PopupWindowMenuItem(
                EDIT_ID,
                PopupWindowMenuItem.Image(drawableRes = R.drawable.ic_popup_menu_edit),
                PopupWindowMenuItem.Title(contentRes = R.string.password_popup_menu_item_edit),
            )
        )

        val pinMenuItem = if (passwordItemModel?.isPinned == true) {
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
                PopupWindowMenuItem.Title(contentRes = R.string.password_popup_menu_item_delete),
                PopupWindowMenuItem.ItemTint(
                    titleTintAttrRes = R.attr.themedErrorColor,
                    iconTintAttrRes = R.attr.themedErrorColor,
                )
            )
        )

        return menuItems
    }

    private companion object {
        const val PASSWORD_SHOW_ACTION_CLICK_DELAY_IN_MILLIS = 700L

        // Popup menu items ids
        const val SELECT_ID = 0
        const val COPY_ID = 1
        const val EDIT_ID = 2
        const val PIN_ID = 3
        const val UNPIN_ID = 4
        const val DELETE_ID = 5
    }
}