package com.ak.base.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.ak.base.constants.AppConstants
import com.google.android.material.appbar.AppBarLayout
import java.util.*

fun Float.dpToPx(context: Context?) =
    if (context != null) {
        TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
            context.applicationContext.resources.displayMetrics
        ).toInt()
    } else {
        this.toInt()
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

inline fun View.setSafeClickListener(
    allowedClickDelayInMillis: Long = AppConstants.VIEW_SAFE_CLICK_DELAY_IN_MILLIS,
    crossinline listener: (view: View?) -> Unit
) {
    setOnClickListener(object : View.OnClickListener {
        private var lastClickTime = 0L

        override fun onClick(v: View?) {
            val clickTimeInMillis = Calendar.getInstance().timeInMillis
            val clickDelayInMillis = clickTimeInMillis - lastClickTime
            Log.d(
                "ClickLogs",
                "lastClick: $lastClickTime, clickTime: $clickTimeInMillis, clickDelay: $clickDelayInMillis"
            )
            if (clickDelayInMillis < allowedClickDelayInMillis) {
                Log.d("ClickLogs", "returned")
                return
            }
            lastClickTime = clickTimeInMillis
            Log.d("ClickLogs", "lambda onClicked")
            listener(v)
        }
    })
}

private fun changeAppBarLayoutScrollBehavior(appBarLayout: AppBarLayout, isScrollNeeds: Boolean) {
    val appBarLayoutParams = appBarLayout.layoutParams as CoordinatorLayout.LayoutParams
    appBarLayoutParams.behavior = if (isScrollNeeds) AppBarLayout.Behavior() else null
    appBarLayout.layoutParams = appBarLayoutParams
}