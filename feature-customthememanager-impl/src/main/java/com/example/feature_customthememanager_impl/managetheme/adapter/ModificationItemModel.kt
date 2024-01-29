package com.example.feature_customthememanager_impl.managetheme.adapter

import androidx.annotation.ColorInt

interface ModificationItemModel {
    val itemId: Int
    val itemName: String
}

data class ColorModificationItemModel(
    override val itemId: Int,
    override val itemName: String,
    @ColorInt val colorIntValue: Int,
    val attrCustomId: Int,
) : ModificationItemModel
