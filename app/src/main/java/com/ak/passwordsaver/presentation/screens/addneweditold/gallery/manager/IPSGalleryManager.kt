package com.ak.passwordsaver.presentation.screens.addneweditold.gallery.manager

import android.content.Intent
import android.graphics.Bitmap
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity

interface IPSGalleryManager {
    fun getLastGalleryImage(): Bitmap?
    fun openGalleryForImagePick(parentActivity: FragmentActivity, fragment: Fragment)
    fun openGalleryForImagePick(activity: AppCompatActivity)
    fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?)
}