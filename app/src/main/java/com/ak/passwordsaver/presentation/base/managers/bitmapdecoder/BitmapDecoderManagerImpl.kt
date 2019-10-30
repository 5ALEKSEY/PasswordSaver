package com.ak.passwordsaver.presentation.base.managers.bitmapdecoder

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.FileNotFoundException
import javax.inject.Inject

class BitmapDecoderManagerImpl @Inject constructor(private val context: Context) :
    IBitmapDecoderManager {

    override fun decodeBitmapFromUriPath(uriPath: String) = try {
        val imageUri = Uri.parse(uriPath)
        val bitmapStream = context.contentResolver.openInputStream(imageUri)
        BitmapFactory.decodeStream(bitmapStream)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
        null
    }
}