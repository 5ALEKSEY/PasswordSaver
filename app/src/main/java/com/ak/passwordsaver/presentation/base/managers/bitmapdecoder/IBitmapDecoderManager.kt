package com.ak.passwordsaver.presentation.base.managers.bitmapdecoder

import android.graphics.Bitmap
import android.media.Image

interface IBitmapDecoderManager {
    fun decodeBitmap(uriPath: String): Bitmap?
    fun decodeBitmap(image: Image): Bitmap?
}