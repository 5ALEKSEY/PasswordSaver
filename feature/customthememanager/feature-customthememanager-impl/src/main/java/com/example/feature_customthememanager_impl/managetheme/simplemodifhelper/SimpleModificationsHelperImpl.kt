package com.example.feature_customthememanager_impl.managetheme.simplemodifhelper

import android.graphics.Color
import androidx.core.graphics.ColorUtils
import com.ak.app_theme.theme.CustomThemeColorAttr
import com.ak.core_repo_api.intefaces.IResourceManager
import com.example.feature_customthememanager_impl.managetheme.adapter.ColorModificationItemModel
import com.example.feature_customthememanager_impl.managetheme.adapter.ModificationItemModel
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SimpleModificationsHelperImpl @Inject constructor(
    private val resManager: IResourceManager,
) : ISimpleModificationsHelper {

    override suspend fun createFullModification(
        simpleModifications: List<ModificationItemModel>,
        isLightTheme: Boolean,
    ) = withContext(Dispatchers.IO) {
        // colors
        val colorsModifications = createColorsModifications(
            simpleModifications.filterIsInstance<ColorModificationItemModel>(),
            isLightTheme,
        )
        // smth other for the future

        return@withContext colorsModifications // colorsModifications + ... + ...
    }

    private suspend fun createColorsModifications(
        simpleModifications: List<ColorModificationItemModel>?,
        isLightTheme: Boolean,
    ) = withContext(Dispatchers.IO) {
        if (simpleModifications.isNullOrEmpty()) {
            return@withContext emptyList<ColorModificationItemModel>()
        }

        val resultModifications = mutableListOf<ColorModificationItemModel>()

        val primaryColorInt = requireNotNull(
            simpleModifications.findColorByCustomAttrId(CustomThemeColorAttr.PRIMARY.attrCustomId)
        )
        val primaryDarkColorInt = primaryColorInt.makeItDarker(0.2F)
        val primaryLightColorInt = primaryColorInt.makeItLighter(0.2F)
        val accentColorInt = primaryColorInt.makeItLighter(0.35F)
        val primaryBgColorInt = (if (isLightTheme) Color.WHITE else Color.BLACK).makeItDarkerIfPossible(PRIMARY_BG_RATIO)

        // just to save the order and not forget some attr
        CustomThemeColorAttr.values().forEachIndexed { index, customColorAttr ->
            when(customColorAttr) {
                CustomThemeColorAttr.PRIMARY -> {
                    simpleModifications.findByCustomAttrId(customColorAttr.attrCustomId)?.let {
                        resultModifications.add(it)
                    }
                }
                CustomThemeColorAttr.PRIMARY_BG -> {
                    val model = ColorModificationItemModel(
                        itemId = index,
                        itemName = resManager.getString(customColorAttr.nameResId),
                        colorIntValue = primaryBgColorInt,
                        attrCustomId = customColorAttr.attrCustomId,
                    )
                    resultModifications.add(model)
                }
                CustomThemeColorAttr.PRIMARY_DARK -> {
                    val model = ColorModificationItemModel(
                        itemId = index,
                        itemName = resManager.getString(customColorAttr.nameResId),
                        colorIntValue = primaryDarkColorInt,
                        attrCustomId = customColorAttr.attrCustomId,
                    )
                    resultModifications.add(model)
                }
                CustomThemeColorAttr.PRIMARY_LIGHT -> {
                    val model = ColorModificationItemModel(
                        itemId = index,
                        itemName = resManager.getString(customColorAttr.nameResId),
                        colorIntValue = primaryLightColorInt,
                        attrCustomId = customColorAttr.attrCustomId,
                    )
                    resultModifications.add(model)
                }
                CustomThemeColorAttr.ACCENT -> {
                    val model = ColorModificationItemModel(
                        itemId = index,
                        itemName = resManager.getString(customColorAttr.nameResId),
                        colorIntValue = accentColorInt,
                        attrCustomId = customColorAttr.attrCustomId,
                    )
                    resultModifications.add(model)
                }
                CustomThemeColorAttr.SECONDARY_BG -> {
                    val model = ColorModificationItemModel(
                        itemId = index,
                        itemName = resManager.getString(customColorAttr.nameResId),
                        colorIntValue = primaryBgColorInt.makeItLighterIfPossible(SECONDARY_BG_RATIO),
                        attrCustomId = customColorAttr.attrCustomId,
                    )
                    resultModifications.add(model)
                }
                CustomThemeColorAttr.POPUP_BG -> {
                    val model = ColorModificationItemModel(
                        itemId = index,
                        itemName = resManager.getString(customColorAttr.nameResId),
                        colorIntValue = primaryBgColorInt.makeItDarkerIfPossible(POPUP_BG_RATIO),
                        attrCustomId = customColorAttr.attrCustomId,
                    )
                    resultModifications.add(model)
                }
                CustomThemeColorAttr.TOOLBARS_ICONS_AND_TEXT -> {
                    val model = ColorModificationItemModel(
                        itemId = index,
                        itemName = resManager.getString(customColorAttr.nameResId),
                        colorIntValue = findContrastedColor(primaryColorInt),
                        attrCustomId = customColorAttr.attrCustomId,
                    )
                    resultModifications.add(model)
                }
                CustomThemeColorAttr.PRIMARY_TEXT -> {
                    val model = ColorModificationItemModel(
                        itemId = index,
                        itemName = resManager.getString(customColorAttr.nameResId),
                        colorIntValue = findContrastedColor(primaryBgColorInt),
                        attrCustomId = customColorAttr.attrCustomId,
                    )
                    resultModifications.add(model)
                }
                CustomThemeColorAttr.PRIMARY_HINT_TEXT -> {
                    val model = ColorModificationItemModel(
                        itemId = index,
                        itemName = resManager.getString(customColorAttr.nameResId),
                        colorIntValue = findContrastedColor(primaryBgColorInt).makeItLighterIfPossible(HINT_TEXT_RATIO),
                        attrCustomId = customColorAttr.attrCustomId,
                    )
                    resultModifications.add(model)
                }
                CustomThemeColorAttr.SECONDARY_TEXT -> {
                    val model = ColorModificationItemModel(
                        itemId = index,
                        itemName = resManager.getString(customColorAttr.nameResId),
                        colorIntValue = findContrastedColor(primaryBgColorInt).makeItLighterIfPossible(SECONDARY_TEXT_RATIO),
                        attrCustomId = customColorAttr.attrCustomId,
                    )
                    resultModifications.add(model)
                }
                CustomThemeColorAttr.DIVIDER -> {
                    val model = ColorModificationItemModel(
                        itemId = index,
                        itemName = resManager.getString(customColorAttr.nameResId),
                        colorIntValue = findContrastedColor(primaryBgColorInt).makeItLighterIfPossible(SECONDARY_TEXT_RATIO),
                        attrCustomId = customColorAttr.attrCustomId,
                    )
                    resultModifications.add(model)
                }
                CustomThemeColorAttr.ERROR -> {
                    val model = ColorModificationItemModel(
                        itemId = index,
                        itemName = resManager.getString(customColorAttr.nameResId),
                        colorIntValue = Color.RED,
                        attrCustomId = customColorAttr.attrCustomId,
                    )
                    resultModifications.add(model)
                }
                CustomThemeColorAttr.SUCCESS -> {
                    val model = ColorModificationItemModel(
                        itemId = index,
                        itemName = resManager.getString(customColorAttr.nameResId),
                        colorIntValue = Color.GREEN,
                        attrCustomId = customColorAttr.attrCustomId,
                    )
                    resultModifications.add(model)
                }
                CustomThemeColorAttr.SWITCH_THUMB_UNCHECKED -> {
                    val model = ColorModificationItemModel(
                        itemId = index,
                        itemName = resManager.getString(customColorAttr.nameResId),
                        colorIntValue = findContrastedColor(primaryBgColorInt),
                        attrCustomId = customColorAttr.attrCustomId,
                    )
                    resultModifications.add(model)
                }
                CustomThemeColorAttr.SWITCH_THUMB_CHECKED -> {
                    val model = ColorModificationItemModel(
                        itemId = index,
                        itemName = resManager.getString(customColorAttr.nameResId),
                        colorIntValue = primaryColorInt,
                        attrCustomId = customColorAttr.attrCustomId,
                    )
                    resultModifications.add(model)
                }
                CustomThemeColorAttr.SWITCH_TRACK_UNCHECKED -> {
                    val model = ColorModificationItemModel(
                        itemId = index,
                        itemName = resManager.getString(customColorAttr.nameResId),
                        colorIntValue = findContrastedColor(primaryBgColorInt).makeItDarkerIfPossible(HINT_TEXT_RATIO),
                        attrCustomId = customColorAttr.attrCustomId,
                    )
                    resultModifications.add(model)
                }
                CustomThemeColorAttr.SWITCH_TRACK_CHECKED -> {
                    val model = ColorModificationItemModel(
                        itemId = index,
                        itemName = resManager.getString(customColorAttr.nameResId),
                        colorIntValue = primaryLightColorInt,
                        attrCustomId = customColorAttr.attrCustomId,
                    )
                    resultModifications.add(model)
                }
                CustomThemeColorAttr.SELECTED_ITEM_GRADIENT_START -> {
                    val model = ColorModificationItemModel(
                        itemId = index,
                        itemName = resManager.getString(customColorAttr.nameResId),
                        colorIntValue = Color.BLACK,
                        attrCustomId = customColorAttr.attrCustomId,
                    )
                    resultModifications.add(model)
                }
                CustomThemeColorAttr.SELECTED_ITEM_GRADIENT_CENTER -> {
                    val model = ColorModificationItemModel(
                        itemId = index,
                        itemName = resManager.getString(customColorAttr.nameResId),
                        colorIntValue = Color.BLACK,
                        attrCustomId = customColorAttr.attrCustomId,
                    )
                    resultModifications.add(model)
                }
                CustomThemeColorAttr.SELECTED_ITEM_GRADIENT_END -> {
                    val model = ColorModificationItemModel(
                        itemId = index,
                        itemName = resManager.getString(customColorAttr.nameResId),
                        colorIntValue = Color.BLACK,
                        attrCustomId = customColorAttr.attrCustomId,
                    )
                    resultModifications.add(model)
                }
            }
        }

        return@withContext resultModifications
    }

    override fun getSimpleModificationColorAttrs() = SIMPLE_CUSTOM_COLORS_ATTR_IDS

    private fun Int.makeItDarker(ratio: Float): Int {
        return ColorUtils.blendARGB(this, Color.BLACK, ratio)
    }

    private fun Int.makeItLighter(ratio: Float): Int {
        return ColorUtils.blendARGB(this, Color.WHITE, ratio)
    }

    private fun Int.makeItDarkerIfPossible(ratio: Float): Int {
        val canMakeItDarker = ColorUtils.calculateLuminance(this) in 0.4F..1.0F
        return if (canMakeItDarker) makeItDarker(ratio) else makeItLighter(ratio)
    }

    private fun Int.makeItLighterIfPossible(ratio: Float): Int {
        val canMakeItLighter = ColorUtils.calculateLuminance(this) in 0.0F..0.6F
        return if (canMakeItLighter) makeItLighter(ratio) else makeItDarker(ratio)
    }

    private fun findContrastedColor(forBackground: Int): Int {
        val isLightCandidate = ColorUtils.calculateLuminance(CONTRAST_COLOR_CANDIDATE) in 0.5F..1.0F
        val findCandidateStepsCount = (1.0F / FIND_CONTRAST_COLOR_RATIO_STEP).roundToInt()
        if (findCandidateStepsCount <= 1) {
            return CONTRAST_COLOR_CANDIDATE
        }

        var resultContrastedColor = CONTRAST_COLOR_CANDIDATE
        var currentMaxContrast = 1.0

        for (i in 0..findCandidateStepsCount) {
            val ratio = i * FIND_CONTRAST_COLOR_RATIO_STEP
            val modifiedCandidate = if (isLightCandidate) {
                CONTRAST_COLOR_CANDIDATE.makeItDarkerIfPossible(ratio)
            } else {
                CONTRAST_COLOR_CANDIDATE.makeItLighterIfPossible(ratio)
            }
            val contrast = ColorUtils.calculateContrast(modifiedCandidate, forBackground)

            if (contrast > currentMaxContrast) {
                currentMaxContrast = contrast
                resultContrastedColor = modifiedCandidate
            }
        }

        return resultContrastedColor
    }

    private fun List<ColorModificationItemModel>.findColorByCustomAttrId(
        attrId: Int,
    ): Int? {
        return findByCustomAttrId(attrId)?.colorIntValue
    }

    private fun List<ColorModificationItemModel>.findByCustomAttrId(
        attrId: Int,
    ): ColorModificationItemModel? {
        return find { it.attrCustomId == attrId }
    }

    private companion object {
        const val PRIMARY_BG_RATIO = 0.2F
        const val SECONDARY_BG_RATIO = 0.2F
        const val POPUP_BG_RATIO = 0.2F
        const val HINT_TEXT_RATIO = 0.3F
        const val SECONDARY_TEXT_RATIO = 0.2F
        const val CONTRAST_COLOR_CANDIDATE = Color.WHITE
        const val FIND_CONTRAST_COLOR_RATIO_STEP = 0.2F
        val SIMPLE_CUSTOM_COLORS_ATTR_IDS = listOf(
            CustomThemeColorAttr.PRIMARY.attrCustomId,
        )
    }
}