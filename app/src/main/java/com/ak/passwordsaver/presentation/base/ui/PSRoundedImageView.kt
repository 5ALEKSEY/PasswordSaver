package com.ak.passwordsaver.presentation.base.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.ImageView


class PSRoundedImageView(context: Context?, attrs: AttributeSet?) : ImageView(context, attrs) {

    override fun onDraw(canvas: Canvas?) {
        val clipPath = Path()
        val rect = RectF(0f, 0f, this.width.toFloat(), this.height.toFloat())
        clipPath.addRoundRect(rect, 30F, 30F, Path.Direction.CW)
        canvas?.clipPath(clipPath)
        super.onDraw(canvas)
    }
}