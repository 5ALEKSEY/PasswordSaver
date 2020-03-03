package com.ak.base.managers.bitmapdecoder

import android.graphics.Bitmap
import android.media.Image

interface IBitmapDecoderManager {
    fun decodeBitmap(uriPath: String): Bitmap?
    fun decodeBitmap(image: Image): Bitmap?
}