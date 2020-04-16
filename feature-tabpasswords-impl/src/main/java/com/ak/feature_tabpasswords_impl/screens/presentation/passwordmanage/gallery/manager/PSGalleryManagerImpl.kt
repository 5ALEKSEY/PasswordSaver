package com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.gallery.manager

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.ak.base.constants.AppConstants
import java.io.File
import javax.inject.Inject

class PSGalleryManagerImpl @Inject constructor(private val context: Context) : IPSGalleryManager {

    private lateinit var onImagePickedFromGallery: (imageUriPath: String) -> Unit

    override fun getLastGalleryImage(): Bitmap? {
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

    override fun openGalleryForImagePick(parentActivity: FragmentActivity, fragment: Fragment) {
        parentActivity.startActivityFromFragment(
            fragment,
            getGalleryPickIntentAction(),
            AppConstants.GALLERY_IMAGE_PICK_REQUEST_CODE
        )
    }

    override fun openGalleryForImagePick(activity: AppCompatActivity) {
        activity.startActivityForResult(
            getGalleryPickIntentAction(),
            AppConstants.GALLERY_IMAGE_PICK_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode != AppConstants.GALLERY_IMAGE_PICK_REQUEST_CODE) {
            return
        }
        if (resultCode == Activity.RESULT_OK && intent != null && intent.data != null) {
            val imageUriPath = intent.data!!.toString()
            context.contentResolver.takePersistableUriPermission(
                intent.data!!,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
            pushPickedImageUriPath(imageUriPath)
        }
    }

    override fun setOnImagePickedListener(listener: (imageUriPath: String) -> Unit) {
        onImagePickedFromGallery = listener
    }

    private fun pushPickedImageUriPath(imageUriPath: String) {
        if (this::onImagePickedFromGallery.isInitialized) {
            onImagePickedFromGallery(imageUriPath)
        }
    }

    private fun getGalleryPickIntentAction() =
        Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = AppConstants.IMAGE_MIME_TYPE
        }
}