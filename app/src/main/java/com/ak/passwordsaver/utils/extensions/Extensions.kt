package com.ak.passwordsaver.utils.extensions

import android.graphics.*
import android.util.TypedValue
import com.ak.passwordsaver.PSApplication
import de.hdodenhof.circleimageview.CircleImageView

fun Float.dpToPx(): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        PSApplication.appInstance.resources.displayMetrics
    ).toInt()
}

fun CircleImageView.drawTextInner(
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

fun CircleImageView.drawTextInner(
    fillColor: Int,
    textColor: Int,
    textSizeInPx: Int,
    textToDraw: String
) {
    drawTextInner(width, fillColor, textColor, textSizeInPx, textToDraw)
}