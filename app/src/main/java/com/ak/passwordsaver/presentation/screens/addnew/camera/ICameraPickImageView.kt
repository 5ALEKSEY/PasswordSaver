package com.ak.passwordsaver.presentation.screens.addnew.camera

import android.graphics.Bitmap
import com.ak.passwordsaver.presentation.base.ui.IBaseAppView

interface ICameraPickImageView: IBaseAppView {
    fun takeImageAction()
    fun displayPreviewImageStrategy(previewBitmap: Bitmap)
    fun displayTakeImageStrategy()
}