package com.ak.base.ui.custom.popupmenu

import android.graphics.drawable.Drawable
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class PopupWindowMenuItem(
    val id: Int,
    val image: Image,
    val title: Title,
    val customItemTint: ItemTint? = null,
) {
    data class Title(
        @StringRes
        val contentRes: Int? = null,
        val content: String? = null,
    )

    data class Image(
        @DrawableRes
        val drawableRes: Int? = null,
        val drawable: Drawable? = null,
    )

    data class ItemTint(
        @AttrRes
        val titleTintAttrRes: Int? = null,
        @AttrRes
        val iconTintAttrRes: Int? = null,
    )
}
