package com.ak.passwordsaver.presentation.screens.addneweditold.camera

import android.graphics.Bitmap
import com.ak.passwordsaver.presentation.base.ui.IBaseAppView

interface ICameraPickImageView: IBaseAppView {
    fun displayPreviewImageStrategy(previewBitmap: Bitmap)
    fun displayTakeImageStrategy()
    fun sendSuccessImagePickResult(filePath: String)
    fun sendCancelResult()
}