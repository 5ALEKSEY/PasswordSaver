package com.ak.passwordsaver.presentation.screens.home

import com.ak.base.ui.IBaseAppView

interface IHomeView : IBaseAppView {
    fun finishScreen()
    fun setFeatureBadgeText(featureMenuId: Int, text: String)
    fun removeFeatureBadgeText(featureMenuId: Int)
}