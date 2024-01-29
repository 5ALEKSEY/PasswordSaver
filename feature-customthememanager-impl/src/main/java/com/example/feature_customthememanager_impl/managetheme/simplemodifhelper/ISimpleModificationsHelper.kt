package com.example.feature_customthememanager_impl.managetheme.simplemodifhelper

import com.example.feature_customthememanager_impl.managetheme.adapter.ModificationItemModel

interface ISimpleModificationsHelper {
    suspend fun createFullModification(
        simpleModifications: List<ModificationItemModel>,
        isLightTheme: Boolean,
    ): List<ModificationItemModel>

    fun getSimpleModificationColorAttrs(): List<Int>
}