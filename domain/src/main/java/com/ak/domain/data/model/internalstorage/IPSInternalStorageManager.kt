package com.ak.domain.data.model.internalstorage

import android.graphics.Bitmap

interface IPSInternalStorageManager {
    fun saveBitmapImage(bitmapImage: Bitmap): String?
    fun getBitmapImageFromPath(filePath: String): Bitmap?
}