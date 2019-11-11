package com.ak.passwordsaver.model.internalstorage

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject


class PSInternalStorageManagerImpl @Inject constructor(
    val context: Context
) : IPSInternalStorageManager {

    companion object {
        private const val BITMAP_COMPRESS_QUALITY = 100
        private const val PRIVATE_IMAGE_DIRECTORY_NAME = "imageDirectory"
        private const val IMAGE_FILE_NAME_PREFIX = "image_"
        private const val IMAGE_FILE_EXTENSION = ".jpg"
    }

    override fun saveToInternalStorage(bitmapImage: Bitmap): String? {
        val fileForSave = createNewImageFile() ?: return null

        var outputStream: FileOutputStream? = null
        try {
            outputStream = FileOutputStream(fileForSave)
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, BITMAP_COMPRESS_QUALITY, outputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                outputStream!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return fileForSave.absolutePath
    }

    private fun createNewImageFile(): File? {
        try {

            val cw = ContextWrapper(context)
            val directory = cw.getDir(PRIVATE_IMAGE_DIRECTORY_NAME, Context.MODE_PRIVATE)
            if (!directory.exists()) {
                directory.mkdirs()
            }

            val timeMillis = System.currentTimeMillis()
            val fileName = IMAGE_FILE_NAME_PREFIX + timeMillis + IMAGE_FILE_EXTENSION
            return File(directory, fileName)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }
}