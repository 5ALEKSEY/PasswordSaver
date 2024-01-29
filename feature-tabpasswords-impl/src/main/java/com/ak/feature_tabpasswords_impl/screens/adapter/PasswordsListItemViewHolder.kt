package com.ak.feature_tabpasswords_impl.screens.adapter

import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import com.ak.feature_tabpasswords_impl.R
import de.hdodenhof.circleimageview.CircleImageView

class PasswordsListItemViewHolder(
    itemView: View,
    private val listener: PasswordsListClickListener,
) : BasePopupMenuRecyclerViewHolder(itemView) {

    private val cvPasswordItemContainer by lazy { itemView.findViewById<View>(R.id.cvPasswordItemContainer) }
    private val ivItemPinned by lazy { itemView.findViewById<View>(R.id.ivItemPinned) }
    private val ivItemSelected by lazy { itemView.findViewById<View>(R.id.ivItemSelected) }
    private val ivPasswordAvatar by lazy { itemView.findViewById<CircleImageView>(R.id.ivPasswordAvatar) }
    private val tvPasswordContent by lazy { itemView.findViewById<TextView>(R.id.tvPasswordContent) }
    private val tvPasswordName by lazy { itemView.findViewById<TextView>(R.id.tvPasswordName) }
    private val vPasswordItemRoot by lazy { itemView.findViewById<ViewGroup>(R.id.vPasswordItemRoot) }

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
            cvPasswordItemContainer,
            R.attr.themedSecondaryBackgroundColor,
        )
        CustomThemeApplier.applyTextColor(
            theme,
            R.attr.themedPrimaryTextColor,
            tvPasswordName,
            tvPasswordContent,
        )
        applySelectedItemIconBackground(theme)
        applyPinItemBackground(theme)
        drawEmptyPasswordAvatarIfNeeds(theme)
        drawPasswordContentText(theme)
    }

    fun bindPasswordListItemView(passwordItemModel: PasswordItemModel, theme: CustomTheme) {
        this.passwordItemModel = passwordItemModel

        tvPasswordName.text = passwordItemModel.name

        tvPasswordContent.apply {
            maxLines = if (passwordItemModel.isPasswordContentVisible) {
                AppConstants.MAX_LINES_VISIBLE_CONTENT
            } else {
                AppConstants.MAX_LINES_INVISIBLE_CONTENT
            }
            drawPasswordContentText(theme)
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

        setRootItemBackground(passwordItemModel, vPasswordItemRoot, theme)

        if (!passwordItemModel.isInActionModeState) {
            ivItemSelected.setVisibilityInvisible(false)
        } else {
            ivItemSelected.apply {
                setVisibilityInvisible(passwordItemModel.isItemSelected)
            }
        }

        if (passwordItemModel.passwordAvatarBitmap != null) {
            ivPasswordAvatar.setImageBitmap(passwordItemModel.passwordAvatarBitmap)
        }
        drawEmptyPasswordAvatarIfNeeds(theme)

        ivItemPinned.isVisible = passwordItemModel.isPinned
    }

    private fun drawPasswordContentText(theme: CustomTheme) {
        val passwordItemModel = passwordItemModel ?: return

        val hiddenItemRadiusPx = itemView.resources.getDimensionPixelSize(R.dimen.password_hidden_item_radius)
        val hiddenItemSizePx = itemView.resources.getDimensionPixelSize(R.dimen.password_hidden_item_size)
        val hiddenItemPaddingPx = itemView.resources.getDimensionPixelSize(R.dimen.password_hidden_item_padding)

        val hiddenContentOvalDrawable = CustomThemeDrawableBuilder(theme, itemView.context)
            .oval()
            .radius(hiddenItemRadiusPx.toFloat())
            .solidColorAttr(R.attr.themedPrimaryTextColor)
            .build()

        val hiddenContentDrawable = CustomThemeDrawableBuilder(theme, itemView.context)
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

        tvPasswordContent.text = PSUtils.getHiddenContentTextTemp(
            passwordItemModel.isPasswordContentVisible,
            passwordItemModel.password,
            hiddenContentDrawable,
        )
    }

    private fun applyPinItemBackground(theme: CustomTheme) {
        ivItemPinned.background = CustomThemeDrawableBuilder(theme, itemView.context)
            .oval()
            .solidColorAttr(R.attr.themedAccentColor)
            .radius(itemView.resources.getDimensionPixelSize(R.dimen.pinned_password_icon_size).toFloat())
            .build()
    }

    private fun applySelectedItemIconBackground(theme: CustomTheme) {
        ivItemSelected.background = CustomThemeDrawableBuilder(theme, itemView.context)
            .oval()
            .solidColorAttr(R.attr.themedAccentColor)
            .radius(itemView.resources.getDimensionPixelSize(R.dimen.pinned_password_selected_item_icon_size).toFloat())
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
        ivPasswordAvatar.apply {
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
            for (i in 0..vPasswordItemRoot.childCount) {
                vPasswordItemRoot.getChildAt(i)?.apply {
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
        if (passwordItemModel.isLoadingModel) {
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