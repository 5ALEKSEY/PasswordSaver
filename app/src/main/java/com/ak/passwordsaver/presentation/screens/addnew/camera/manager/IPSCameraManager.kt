package com.ak.passwordsaver.presentation.screens.addnew.camera.manager

import android.view.TextureView

interface IPSCameraManager {
    fun initCameraManager(isPreviewOnly: Boolean, previewImageView: TextureView)
    fun openCamera()
    fun closeCamera()
    fun switchCamera()
    fun takeImage()
    fun isFacingBackCameraExist(): Boolean
    fun isFacingFrontCameraExist(): Boolean
}