package com.example.feature_customthememanager_impl.managetheme.mapper

import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.CustomThemeColorAttr
import com.ak.base.extensions.intToHexColor
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.core_repo_api.intefaces.theme.CustomUserThemeRepoEntity
import com.ak.core_repo_api.intefaces.theme.ThemeColorAttr
import com.example.feature_customthememanager_impl.managetheme.adapter.ColorModificationItemModel
import com.example.feature_customthememanager_impl.managetheme.adapter.ModificationItemModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CustomThemeMapperImpl @Inject constructor(
    private val resManager: IResourceManager,
) : ICustomThemeMapper {

    override suspend fun customThemeToModificationsList(
        theme: CustomTheme,
    ) = withContext(Dispatchers.IO) {
        CustomThemeColorAttr.values().mapIndexed { index, attr ->
            ColorModificationItemModel(
                itemId = index,
                itemName = resManager.getString(attr.nameResId),
                colorIntValue = theme.getColor(attr.androidAttrId),
                attrCustomId = attr.attrCustomId,
            )
        }.toList()
    }

    override suspend fun modificationsToRepoEntity(
        themeId: Long,
        themeName: String,
        isLightTheme: Boolean,
        modifications: List<ModificationItemModel>,
    ) = withContext(Dispatchers.IO) {
        val colorsModifications = modifications.filterIsInstance<ColorModificationItemModel>()
        val colorAttrs = colorsModifications.map {
            ThemeColorAttr(it.attrCustomId, it.colorIntValue.intToHexColor())
        }

        object : CustomUserThemeRepoEntity {
            override fun getThemeId() = themeId

            override fun getName() = themeName

            override fun isLight() = isLightTheme

            override fun getColorAttrs(): List<ThemeColorAttr> = colorAttrs
        }
    }
}