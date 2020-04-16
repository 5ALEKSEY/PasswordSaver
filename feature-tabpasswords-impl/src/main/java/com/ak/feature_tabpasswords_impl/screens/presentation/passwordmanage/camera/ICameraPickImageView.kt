package com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.camera

import android.graphics.Bitmap
import com.ak.base.ui.IBaseAppView

interface ICameraPickImageView: IBaseAppView {
    fun displayPreviewImageStrategy(previewBitmap: Bitmap)
    fun displayTakeImageStrategy()
    fun sendSuccessImagePickResult(filePath: String)
    fun sendCancelResult()
}