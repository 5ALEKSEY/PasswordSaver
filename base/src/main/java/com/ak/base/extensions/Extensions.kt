package com.ak.base.extensions

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.ak.base.R
import com.ak.base.constants.AppConstants
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.rxjava3.core.SingleEmitter
import java.util.Calendar
import kotlinx.android.synthetic.main.layout_notification_badge.view.tvBadgeText

fun <T : View> T.applyForLaidOut(block: T.() -> Unit) {
    if (isLaidOut) {
        block(this)
    }
}

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
    context: Context,
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
        typeface = context.getFontCompat(R.font.app_font_family)
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
    }
    canvas.drawBitmap(bitmap, 0F, 0F, paint)

    val xPos = bitmap.width / 2
    val yPos = (bitmap.height / 2 - (paint.descent() + paint.ascent()) / 2).toInt()
    canvas.drawText(textToDraw, xPos.toFloat(), yPos.toFloat(), paint)
    setImageBitmap(bitmap)
}

fun ImageView.drawTextInner(
    context: Context,
    fillColor: Int,
    textColor: Int,
    textSizeInPx: Int,
    textToDraw: String
) {
    drawTextInner(context, width, fillColor, textColor, textSizeInPx, textToDraw)
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

fun BottomNavigationView.setTextBadgeByMenuId(menuId: Int, badgeContent: String) {
    getBottomNavViewMenuItem(this, menuId)?.apply {
        val badgeView = LayoutInflater.from(this.context).inflate(R.layout.layout_notification_badge, this, true)
        badgeView.tvBadgeText.text = badgeContent
        setTag(R.id.TAG_BADGE_VIEW, badgeView.tvBadgeText)
    }
}

fun BottomNavigationView.removeTextBadgeByMenuId(menuId: Int) {
    getBottomNavViewMenuItem(this, menuId)?.apply {
        val badgeView = getTag(R.id.TAG_BADGE_VIEW) as? View ?: return
        removeView(badgeView)
        setTag(R.id.TAG_BADGE_VIEW, null)
    }
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

fun <T> SingleEmitter<T>.onSuccessSafe(value: T) {
    if (!isDisposed) {
        onSuccess(value)
    }
}

fun <T> SingleEmitter<T>.onErrorSafe(error: Throwable) {
    if (!isDisposed) {
        onError(error)
    }
}

fun Int.toColorStateList() = ColorStateList.valueOf(this)

private fun getBottomNavViewMenuItem(bottomNavigationView: BottomNavigationView, menuItemId: Int): BottomNavigationItemView? {
    val menuItemView = bottomNavigationView.getChildAt(0)?.findViewById<View>(menuItemId)
    return if (menuItemView !is BottomNavigationItemView) {
        null
    } else {
        menuItemView
    }
}