package com.ak.passwordsaver.model.internalstorage

import android.graphics.Bitmap

interface IPSInternalStorageManager {
    fun saveToInternalStorage(bitmapImage: Bitmap): String?
}