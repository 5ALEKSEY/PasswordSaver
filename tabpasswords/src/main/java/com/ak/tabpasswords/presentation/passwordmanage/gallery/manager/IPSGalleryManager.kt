package com.ak.tabpasswords.presentation.passwordmanage.gallery.manager

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

interface IPSGalleryManager {
    fun getLastGalleryImage(): Bitmap?
    fun openGalleryForImagePick(parentActivity: FragmentActivity, fragment: Fragment)
    fun openGalleryForImagePick(activity: AppCompatActivity)
    fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?)
    fun setOnImagePickedListener(listener: (imageUriPath: String) -> Unit)
}