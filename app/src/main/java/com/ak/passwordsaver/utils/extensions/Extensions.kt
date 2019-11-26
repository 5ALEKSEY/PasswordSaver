package com.ak.passwordsaver.utils.extensions

import android.graphics.*
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.Toolbar
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import com.ak.passwordsaver.PSApplication
import android.graphics.Bitmap
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.graphics.RectF








fun Float.dpToPx(): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        PSApplication.appInstance.resources.displayMetrics
    ).toInt()
}

fun ImageView.drawTextInner(
    imageSizeInPx: Int,
    fillColor: Int,
    textColor: Int,
    textSizeInPx: Int,
    textToDraw: String
) {
    val bitmap = Bitmap.createBitmap(imageSizeInPx, imageSizeInPx, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    canvas.drawColor(fillColor)
    val paint = Paint().apply {
        color = textColor
        textAlign = Paint.Align.CENTER
        textSize = textSizeInPx.toFloat()
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
    }
    canvas.drawBitmap(bitmap, 0F, 0F, paint)

    val xPos = bitmap.width / 2
    val yPos = (bitmap.height / 2 - (paint.descent() + paint.ascent()) / 2).toInt()
    canvas.drawText(textToDraw, xPos.toFloat(), yPos.toFloat(), paint)
    setImageBitmap(bitmap)
}

fun ImageView.drawTextInner(
    fillColor: Int,
    textColor: Int,
    textSizeInPx: Int,
    textToDraw: String
) {
    drawTextInner(width, fillColor, textColor, textSizeInPx, textToDraw)
}

fun Toolbar.turnOffToolbarScrolling(appBarLayout: AppBarLayout) {
    //turn off scrolling
    val toolbarLayoutParams = layoutParams as AppBarLayout.LayoutParams
    toolbarLayoutParams.scrollFlags = 0
    layoutParams = toolbarLayoutParams

    changeAppBarLayoutScrollBehavior(appBarLayout, false)
}

fun Toolbar.turnOnToolbarScrolling(appBarLayout: AppBarLayout) {
    //turn on scrolling
    val toolbarLayoutParams = layoutParams as AppBarLayout.LayoutParams
    toolbarLayoutParams.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
            AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
    layoutParams = toolbarLayoutParams

    changeAppBarLayoutScrollBehavior(appBarLayout, true)
}

fun View.setVisibility(isVisible: Boolean) {
    visibility = if (isVisible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

fun View.setVisibilityInvisible(isVisible: Boolean) {
    visibility = if (isVisible) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}

fun Bitmap.roundBitmap(radiusValue: Int): Bitmap {
    val dstBitmap = Bitmap.createBitmap(
        width,
        height,
        Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(dstBitmap)
    val paint = Paint()
    paint.isAntiAlias = true

    val rect = Rect(0, 0, width, height)
    val rectF = RectF(rect)
    canvas.drawRoundRect(rectF, radiusValue.toFloat(), radiusValue.toFloat(), paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(this, 0F, 0F, paint)
    recycle()
    return dstBitmap
}

private fun changeAppBarLayoutScrollBehavior(appBarLayout: AppBarLayout, isScrollNeeds: Boolean) {
    val appBarLayoutParams = appBarLayout.layoutParams as CoordinatorLayout.LayoutParams
    appBarLayoutParams.behavior = if (isScrollNeeds) AppBarLayout.Behavior() else null
    appBarLayout.layoutParams = appBarLayoutParams
}