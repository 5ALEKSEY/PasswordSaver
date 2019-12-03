package com.ak.passwordsaver.data.model.internalstorage

import android.graphics.Bitmap

interface IPSInternalStorageManager {
    fun saveBitmapImage(bitmapImage: Bitmap): String?
    fun getBitmapImageFromPath(filePath: String): Bitmap?
}