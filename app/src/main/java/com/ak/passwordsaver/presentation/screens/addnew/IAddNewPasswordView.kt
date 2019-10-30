package com.ak.passwordsaver.presentation.screens.addnew

import android.graphics.Bitmap
import com.ak.passwordsaver.presentation.base.ui.IBaseAppView

interface IAddNewPasswordView : IBaseAppView {
    fun displaySuccessPasswordSave()
    fun displayPasswordNameInputError(errorMessage: String)
    fun hidePasswordNameInputError()
    fun displayPasswordContentInputError(errorMessage: String)
    fun hidePasswordContentInputError()
    fun drawTextForPasswordAvatar(text: String)
    fun displayPasswordAvatarChooserImage(bitmapImage: Bitmap?)
    fun deletePasswordAvatarChooserImage()
    fun dismissPasswordAvatarChooserDialog()
    fun openGalleryForImagePick()
    fun openCameraForImagePick()
}