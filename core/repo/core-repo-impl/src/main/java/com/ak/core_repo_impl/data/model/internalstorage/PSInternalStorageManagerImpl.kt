package com.ak.core_repo_impl.data.model.internalstorage

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.ak.core_repo_api.intefaces.IPSInternalStorageManager
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject


class PSInternalStorageManagerImpl @Inject constructor(
    private val context: Context,
) : IPSInternalStorageManager {

    companion object {
        private const val BITMAP_COMPRESS_QUALITY = 100
        private const val PRIVATE_IMAGE_DIRECTORY_NAME = "imageDirectory"
        private const val IMAGE_FILE_NAME_PREFIX = "image_"
        private const val IMAGE_FILE_EXTENSION = ".jpg"
    }

    override fun saveBitmapImage(bitmapImage: Bitmap): String? {
        val fileForSave = createNewImageFile() ?: return null

        var outputStream: OutputStream? = null
        try {
            outputStream = FileOutputStream(fileForSave)
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, BITMAP_COMPRESS_QUALITY, outputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                outputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return fileForSave.absolutePath
    }

    override fun getBitmapImageFromPath(filePath: String): Bitmap? {
        var inputStream: InputStream? = null
        try {
            inputStream = FileInputStream(filePath)
            return BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                inputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
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