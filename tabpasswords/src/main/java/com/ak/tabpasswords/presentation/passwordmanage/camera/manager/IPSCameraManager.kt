package com.ak.tabpasswords.presentation.passwordmanage.camera.manager

import android.graphics.Bitmap
import android.view.TextureView

interface IPSCameraManager {
    fun initCameraManager(isPreviewOnly: Boolean, previewImageView: TextureView)
    fun openCamera()
    fun closeCamera()
    fun switchCamera()
    fun takeImage(onImageCreatedListener: (imageBitmap: Bitmap) -> Unit)
    fun isFacingBackCameraExist(): Boolean
    fun isFacingFrontCameraExist(): Boolean
}