package com.ak.core_repo_impl.theme

import com.ak.core_repo_api.intefaces.theme.ThemeColorAttr
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

private const val PRE_STORED_ID_KEY = "pre_stored_id_key"
private const val PRE_STORED_NAME_KEY = "pre_stored_name_key"
private const val PRE_STORED_IS_LIGHT_KEY = "pre_stored_is_light_key"
private const val PRE_STORED_COLOR_ATTRS_KEY = "pre_stored_color_attrs_key"

@Serializable
data class CustomUserThemePreStoredEntity(
    @SerialName(PRE_STORED_ID_KEY)
    val id: Long,
    @SerialName(PRE_STORED_NAME_KEY)
    val name: String,
    @SerialName(PRE_STORED_IS_LIGHT_KEY)
    val isLight: Boolean,
    @SerialName(PRE_STORED_COLOR_ATTRS_KEY)
    val colorAttrs: List<ThemeColorAttr>,
)
