package com.ak.feature_tabpasswords_impl.screens.adapter

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
import com.ak.feature_tabpasswords_impl.R
import kotlinx.android.synthetic.main.passwords_item_layout.view.*

class PasswordsListItemViewHolder(
    itemView: View,
    private val listener: PasswordsListClickListener
) : RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

    private var passwordItemModel: PasswordItemModel? = null

    private companion object {
        const val PASSWORD_SHOW_ACTION_CLICK_DELAY_IN_MILLIS = 700L

        const val CONTEXT_MENU_SELECT_ID = 1
        const val CONTEXT_MENU_COPY_ID = 2
        const val CONTEXT_MENU_EDIT_ID = 3
        const val CONTEXT_MENU_DELETE_ID = 4
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            CONTEXT_MENU_SELECT_ID -> {
                passwordItemModel?.let {
                    listener.selectPasswordItem(it)
                    true
                } ?: false
            }
            CONTEXT_MENU_COPY_ID -> {
                passwordItemModel?.let {
                    listener.copyPasswordItemContent(it)
                    true
                } ?: false
            }
            CONTEXT_MENU_EDIT_ID -> {
                passwordItemModel?.let {
                    listener.editPasswordItem(it)
                    true
                } ?: false
            }
            CONTEXT_MENU_DELETE_ID -> {
                passwordItemModel?.let {
                    listener.deletePasswordItem(it)
                    true
                } ?: false
            }
            else -> false
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        passwordItemModel?.let { listener.onCreateContextMenuForPasswordItem(it) }
        menu?.apply {
            addContextMenuItem(this, CONTEXT_MENU_SELECT_ID, "Select")
            addContextMenuItem(this, CONTEXT_MENU_COPY_ID, "Copy password")
            addContextMenuItem(this, CONTEXT_MENU_EDIT_ID, "Edit")
            addContextMenuItem(this, CONTEXT_MENU_DELETE_ID, "Delete")
        }
    }

    private fun addContextMenuItem(menu: ContextMenu, itemId: Int, itemTitle: String) {
        menu.add(Menu.NONE, itemId, Menu.NONE, itemTitle).setOnMenuItemClickListener(this)
    }

    fun onClear() {
        itemView.setOnCreateContextMenuListener(null)
    }

    fun bindPasswordListItemView(passwordItemModel: PasswordItemModel) {
        this.passwordItemModel = passwordItemModel
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

        if (passwordItemModel.isInActionModeState) {
            itemView.setOnClickListener {
                listener.selectPasswordItem(passwordItemModel)
            }
        } else {
            itemView.setOnClickListener(null)
        }

        itemView.setOnCreateContextMenuListener(this)

        if (passwordItemModel.isInActionModeState) {
            itemView.setOnLongClickListener {
                listener.selectPasswordItem(passwordItemModel)
                return@setOnLongClickListener true
            }
        } else {
            itemView.setOnLongClickListener(null)
        }

        initAdditionalItemClickListeners(passwordItemModel)

        setRootItemBackground(passwordItemModel, itemView.vPasswordItemRoot)

        if (!passwordItemModel.isInActionModeState) {
            itemView.ivItemSelected.setVisibilityInvisible(false)
        } else {
            itemView.ivItemSelected.setVisibilityInvisible(passwordItemModel.isItemSelected)
        }

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
                listener.showPasswordItemContent(passwordItemModel)
            }
        }
    }

    private fun setRootItemBackground(passwordItemModel: PasswordItemModel, rootView: View) {
        val bgResId = when {
            passwordItemModel.isLoadingModel -> R.drawable.loading_animation
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