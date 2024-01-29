package com.ak.core_repo_api.intefaces.theme

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

private const val ATTR_CUSTOM_ID_KEY = "attr_custom_id"
private const val COLOR_HEX_VALUE_KEY = "color_hex_value"

@Serializable
data class ThemeColorAttr(
    @SerialName(ATTR_CUSTOM_ID_KEY)
    val attrCustomId: Int,
    @SerialName(COLOR_HEX_VALUE_KEY)
    val colorHexValue: String,
)