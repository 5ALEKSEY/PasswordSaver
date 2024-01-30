package com.ak.core_repo_api.intefaces

import android.graphics.Bitmap

interface IPSInternalStorageManager {
    fun saveBitmapImage(bitmapImage: Bitmap): String?
    fun getBitmapImageFromPath(filePath: String): Bitmap?
}