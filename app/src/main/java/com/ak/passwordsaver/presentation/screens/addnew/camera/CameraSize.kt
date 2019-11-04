package com.ak.passwordsaver.presentation.screens.addnew.camera

class CameraSize(val width: Int, val height: Int) {
    val widthRation
        get() = when (height != 0) {
            true -> width.toFloat() / height
            false -> 1.0f
        }

    val heightRatio
        get() = when (width != 0) {
            true -> height.toFloat() / width
            false -> 1.0f
        }
}