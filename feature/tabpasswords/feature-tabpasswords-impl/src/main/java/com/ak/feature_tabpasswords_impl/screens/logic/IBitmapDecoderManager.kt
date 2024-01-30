package com.ak.feature_tabpasswords_impl.screens.logic

import android.graphics.Bitmap
import android.media.Image

interface IBitmapDecoderManager {
    fun decodeBitmap(uriPath: String): Bitmap?
    fun decodeBitmap(image: Image): Bitmap?
}