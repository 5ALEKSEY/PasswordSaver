package com.ak.passwordsaver.presentation.base.managers.bitmapdecoder

import android.graphics.Bitmap

interface IBitmapDecoderManager {
    fun decodeBitmapFromUriPath(uriPath: String): Bitmap?
}