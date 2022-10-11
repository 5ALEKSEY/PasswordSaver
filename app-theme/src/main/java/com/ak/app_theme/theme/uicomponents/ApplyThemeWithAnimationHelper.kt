package com.ak.app_theme.theme.uicomponents

import android.animation.Animator
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.ImageView
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible
import kotlin.math.hypot

object ApplyThemeWithAnimationHelper {

    private const val CHANGE_THEME_ANIMATION_DURATION_MILLIS = 300L
    private const val CHANGE_WINDOW_THEME_DELAY_MILLIS = 140L

    fun changeThemeWithAnimation(
        contentView: View?,
        stubView: ImageView?,
        applyThemeWithoutWindowCallback: () -> Unit,
        applyThemeForWindowCallback: () -> Unit,
    ): Boolean {
        contentView ?: return false
        stubView ?: return false

        if (stubView.isVisible) return false

        val contentWidth = contentView.measuredWidth
        val contentHeight = contentView.measuredHeight

        if (contentWidth <= 0 || contentHeight <= 0) return false

        val stubThemeBitmap = runCatching {
            val bitmap = Bitmap.createBitmap(contentWidth, contentHeight, Bitmap.Config.ARGB_8888)
            contentView.draw(Canvas(bitmap))
            return@runCatching bitmap
        }.getOrNull() ?: return false

        stubView.applyStubThemeView(stubThemeBitmap)

        applyThemeWithoutWindowCallback()

        createChangeThemeAnimator(contentView, contentWidth, contentHeight).apply {
            doOnEnd { stubView.applyStubThemeView(null) }
            contentView.postDelayed({ applyThemeForWindowCallback() }, CHANGE_WINDOW_THEME_DELAY_MILLIS)
        }.start()

        return true
    }

    private fun createChangeThemeAnimator(
        viewForAnimation: View,
        viewForAnimationWidth: Int,
        viewForAnimationHeight: Int,
    ): Animator {
        val finalRadius = hypot(viewForAnimationWidth.toFloat(), viewForAnimationHeight.toFloat())

        return ViewAnimationUtils.createCircularReveal(
            viewForAnimation,
            viewForAnimationWidth / 2,
            viewForAnimationHeight / 2,
            0f,
            finalRadius,
        ).apply { duration = CHANGE_THEME_ANIMATION_DURATION_MILLIS }
    }

    private fun ImageView.applyStubThemeView(stubBitmap: Bitmap?) {
        setImageBitmap(stubBitmap)
        isVisible = stubBitmap != null
    }
}