package com.ak.passwordsaver.presentation.screens.addnew.gallery

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import com.ak.passwordsaver.presentation.base.constants.AppConstants
import java.io.File


class PSGalleryManager constructor(private val context: Context) {

    lateinit var onImagePickedFromGallery: (imageUriPath: String) -> Unit

    fun getLastGalleryImage(): Bitmap? {
        val projection = arrayOf(
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
            MediaStore.Images.ImageColumns.DATE_TAKEN,
            MediaStore.Images.ImageColumns.MIME_TYPE
        )
        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC"
        ) ?: return null

        // Put it in the image view
        if (cursor.moveToFirst()) {
            val imageLocation = cursor.getString(1)
            val imageFile = File(imageLocation)
            if (imageFile.exists()) {
                return BitmapFactory.decodeFile(imageLocation)
            }
        }
        cursor.close()
        return null
    }

    fun openGalleryForImagePick(parentActivity: FragmentActivity, fragment: Fragment) {
        parentActivity.startActivityFromFragment(
            fragment,
            getGalleryPickIntentAction(),
            AppConstants.GALLERY_IMAGE_PICK_REQUEST_CODE
        )
    }

    fun openGalleryForImagePick(activity: AppCompatActivity) {
        activity.startActivityForResult(
            getGalleryPickIntentAction(),
            AppConstants.GALLERY_IMAGE_PICK_REQUEST_CODE
        )
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode != AppConstants.GALLERY_IMAGE_PICK_REQUEST_CODE) {
            return
        }
        if (resultCode == Activity.RESULT_OK && intent != null && intent.data != null) {
            pushPickedImageUriPath(intent.data!!.toString())
        }
    }

    private fun pushPickedImageUriPath(imageUriPath: String) {
        if (this::onImagePickedFromGallery.isInitialized) {
            onImagePickedFromGallery.invoke(imageUriPath)
        }
    }

    private fun getGalleryPickIntentAction() =
        Intent(Intent.ACTION_PICK).apply {
            type = AppConstants.IMAGE_MIME_TYPE
        }
}