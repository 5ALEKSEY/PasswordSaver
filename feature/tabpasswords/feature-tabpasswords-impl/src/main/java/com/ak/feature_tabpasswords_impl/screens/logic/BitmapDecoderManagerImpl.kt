package com.ak.feature_tabpasswords_impl.screens.logic

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import java.io.FileNotFoundException
import javax.inject.Inject

class BitmapDecoderManagerImpl @Inject constructor(private val context: Context) :
    IBitmapDecoderManager {

    override fun decodeBitmap(uriPath: String) = try {
        val imageUri = Uri.parse(uriPath)
        val bitmapStream = context.contentResolver.openInputStream(imageUri)
        BitmapFactory.decodeStream(bitmapStream)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
        null
    }

    override fun decodeBitmap(image: Image): Bitmap? {
        val buffer = image.planes[0].buffer
        val size = buffer.capacity()

        if (size == 0) {
            return null
        }

        val bytes = ByteArray(size)
        buffer.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, null)
    }
}