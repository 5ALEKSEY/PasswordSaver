package com.ak.app_theme.theme

import androidx.annotation.AttrRes
import androidx.annotation.StringRes
import com.ak.app_theme.R

// region Custom ids
const val PRIMARY_BG_ID = 0
const val SECONDARY_BG_ID = PRIMARY_BG_ID + 1
const val POPUP_BG_ID = SECONDARY_BG_ID + 1
const val PRIMARY_ID = POPUP_BG_ID + 1
const val PRIMARY_LIGHT_ID = PRIMARY_ID + 1
const val PRIMARY_DARK_ID = PRIMARY_LIGHT_ID + 1
const val ACCENT_ID = PRIMARY_DARK_ID + 1

const val TOOLBARS_ICONS_AND_TEXT_ID = ACCENT_ID + 1
const val PRIMARY_TEXT_ID = TOOLBARS_ICONS_AND_TEXT_ID + 1
const val PRIMARY_HINT_TEXT_ID = PRIMARY_TEXT_ID + 1
const val SECONDARY_TEXT_ID = PRIMARY_HINT_TEXT_ID + 1
const val DIVIDER_ID = SECONDARY_TEXT_ID + 1
const val ERROR_ID = DIVIDER_ID + 1
const val SUCCESS_ID = ERROR_ID + 1

const val SWITCH_THUMB_UNCHECKED_ID = SUCCESS_ID + 1
const val SWITCH_THUMB_CHECKED_ID = SWITCH_THUMB_UNCHECKED_ID + 1
const val SWITCH_TRACK_UNCHECKED_ID = SWITCH_THUMB_CHECKED_ID + 1
const val SWITCH_TRACK_CHECKED_ID = SWITCH_TRACK_UNCHECKED_ID + 1
const val SELECTED_ITEM_GRADIENT_START_ID = SWITCH_TRACK_CHECKED_ID + 1
const val SELECTED_ITEM_GRADIENT_CENTER_ID = SELECTED_ITEM_GRADIENT_START_ID + 1
const val SELECTED_ITEM_GRADIENT_END_ID = SELECTED_ITEM_GRADIENT_CENTER_ID + 1
// endregion

enum class CustomThemeColorAttr(
    @StringRes
    val nameResId: Int,
    @AttrRes
    val androidAttrId: Int,
    val attrCustomId: Int,
) {
    PRIMARY_BG(
        nameResId = R.string.primary_bg_color_name,
        androidAttrId = R.attr.themedPrimaryBackgroundColor,
        attrCustomId = PRIMARY_BG_ID,
    ),
    SECONDARY_BG(
        nameResId = R.string.secondary_bg_color_name,
        androidAttrId = R.attr.themedSecondaryBackgroundColor,
        attrCustomId = SECONDARY_BG_ID,
    ),
    POPUP_BG(
        nameResId = R.string.popup_color_name,
        androidAttrId = R.attr.themedPopupBackgroundColor,
        attrCustomId = POPUP_BG_ID,
    ),
    PRIMARY(
        nameResId = R.string.primary_color_name,
        androidAttrId = R.attr.themedPrimaryColor,
        attrCustomId = PRIMARY_ID,
    ),
    PRIMARY_DARK(
        nameResId = R.string.primary_dark_color_name,
        androidAttrId = R.attr.themedPrimaryDarkColor,
        attrCustomId = PRIMARY_DARK_ID,
    ),
    PRIMARY_LIGHT(
        nameResId = R.string.primary_light_color_name,
        androidAttrId = R.attr.themedPrimaryLightColor,
        attrCustomId = PRIMARY_LIGHT_ID,
    ),
    ACCENT(
        nameResId = R.string.accent_color_name,
        androidAttrId = R.attr.themedAccentColor,
        attrCustomId = ACCENT_ID,
    ),
    TOOLBARS_ICONS_AND_TEXT(
        nameResId = R.string.toolbars_icons_and_text_color_name,
        androidAttrId = R.attr.themedToolbarIconsAndTextColor,
        attrCustomId = TOOLBARS_ICONS_AND_TEXT_ID,
    ),
    PRIMARY_TEXT(
        nameResId = R.string.primary_text_color_name,
        androidAttrId = R.attr.themedPrimaryTextColor,
        attrCustomId = PRIMARY_TEXT_ID,
    ),
    PRIMARY_HINT_TEXT(
        nameResId = R.string.primary_hint_text_color_name,
        androidAttrId = R.attr.themedPrimaryTextHintColor,
        attrCustomId = PRIMARY_HINT_TEXT_ID,
    ),
    SECONDARY_TEXT(
        nameResId = R.string.secondary_text_color_name,
        androidAttrId = R.attr.themedSecondaryTextColor,
        attrCustomId = SECONDARY_TEXT_ID,
    ),
    DIVIDER(
        nameResId = R.string.divider_color_name,
        androidAttrId = R.attr.themedDividerColor,
        attrCustomId = DIVIDER_ID,
    ),
    ERROR(
        nameResId = R.string.error_color_name,
        androidAttrId = R.attr.themedErrorColor,
        attrCustomId = ERROR_ID,
    ),
    SUCCESS(
        nameResId = R.string.success_color_name,
        androidAttrId = R.attr.themedSuccessColor,
        attrCustomId = SUCCESS_ID,
    ),
    SWITCH_THUMB_UNCHECKED(
        nameResId = R.string.switch_thumb_unchecked_color_name,
        androidAttrId = R.attr.themedSwitchThumbUncheckedColor,
        attrCustomId = SWITCH_THUMB_UNCHECKED_ID,
    ),
    SWITCH_THUMB_CHECKED(
        nameResId = R.string.switch_thumb_checked_color_name,
        androidAttrId = R.attr.themedSwitchThumbCheckedColor,
        attrCustomId = SWITCH_THUMB_CHECKED_ID,
    ),
    SWITCH_TRACK_UNCHECKED(
        nameResId = R.string.switch_track_unchecked_color_name,
        androidAttrId = R.attr.themedSwitchTrackUncheckedColor,
        attrCustomId = SWITCH_TRACK_UNCHECKED_ID,
    ),
    SWITCH_TRACK_CHECKED(
        nameResId = R.string.switch_track_checked_color_name,
        androidAttrId = R.attr.themedSwitchTrackCheckedColor,
        attrCustomId = SWITCH_TRACK_CHECKED_ID,
    ),
    SELECTED_ITEM_GRADIENT_START(
        nameResId = R.string.selected_item_gradient_start_color_name,
        androidAttrId = R.attr.themedSelectedGradientStartColor,
        attrCustomId = SELECTED_ITEM_GRADIENT_START_ID,
    ),
    SELECTED_ITEM_GRADIENT_CENTER(
        nameResId = R.string.selected_item_gradient_center_color_name,
        androidAttrId = R.attr.themedSelectedGradientCenterColor,
        attrCustomId = SELECTED_ITEM_GRADIENT_CENTER_ID,
    ),
    SELECTED_ITEM_GRADIENT_END(
        nameResId = R.string.selected_item_gradient_end_color_name,
        androidAttrId = R.attr.themedSelectedGradientEndColor,
        attrCustomId = SELECTED_ITEM_GRADIENT_END_ID,
    ),
    ;

    companion object {
        fun customIdToAndroid(customId: Int) = values().find {
            it.attrCustomId == customId
        }?.androidAttrId ?: R.attr.themedPrimaryColor
    }
}