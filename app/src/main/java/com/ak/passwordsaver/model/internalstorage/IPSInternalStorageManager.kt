package com.ak.passwordsaver.model.internalstorage

import android.graphics.Bitmap

interface IPSInternalStorageManager {
    fun saveBitmapImage(bitmapImage: Bitmap): String?
    fun getBitmapIamageFromPath(filePath: String): Bitmap?
}