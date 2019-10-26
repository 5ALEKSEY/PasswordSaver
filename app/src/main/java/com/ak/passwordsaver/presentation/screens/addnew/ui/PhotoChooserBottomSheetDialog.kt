package com.ak.passwordsaver.presentation.screens.addnew.ui

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.screens.addnew.camera.PSCameraManager
import com.ak.passwordsaver.presentation.screens.addnew.gallery.PSGalleryManager

class PhotoChooserBottomSheetDialog : BottomSheetDialogFragment() {

    companion object {
        private const val BOTTOM_SHEET_DIALOG_TAG = "PhotoChooserBottomSheetDialog"
        const val CAMERA_CHOOSE_ACTION_ID = 1
        const val GALLERY_CHOOSE_ACTION_ID = 2

        fun show(fragmentManager: FragmentManager): PhotoChooserBottomSheetDialog {
            val sheetDialogInstance = PhotoChooserBottomSheetDialog()
            sheetDialogInstance.show(fragmentManager, BOTTOM_SHEET_DIALOG_TAG)
            return sheetDialogInstance
        }
    }

    lateinit var onChooseAvatarActionListener: (chooseActionId: Int) -> Unit

    private var mFragmentView: View? = null
    private lateinit var mCameraPreviewView: TextureView
    private lateinit var mCameraManager: PSCameraManager
    private lateinit var mGalleryManager: PSGalleryManager

    override fun getTheme() = R.style.BaseBottomSheetDialog

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mFragmentView = inflater.inflate(R.layout.layout_photo_chooser_button, container, false)
        return mFragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mCameraPreviewView = view.findViewById(R.id.texv_photo_chooser_camera_preview)
        context?.let {
            mCameraManager = PSCameraManager(context!!, true, mCameraPreviewView)
            mGalleryManager = PSGalleryManager(context!!)
        }
        view.findViewById<ImageView>(R.id.iv_photo_chooser_gallery_preview).apply {
            val lastGalleryImage = mGalleryManager.getLastGalleryImage()
            if (lastGalleryImage != null) {
                setImageBitmap(lastGalleryImage)
            } else {
                @ColorInt
                val noLastImageColor = ContextCompat.getColor(context!!, R.color.colorBlack)
                setImageDrawable(ColorDrawable(noLastImageColor))
            }
        }
        view.findViewById<View>(R.id.v_choose_camera_action).setOnClickListener {
            if (this::onChooseAvatarActionListener.isInitialized) {
                onChooseAvatarActionListener.invoke(CAMERA_CHOOSE_ACTION_ID)
            }
        }
        view.findViewById<View>(R.id.v_choose_gallery_action).setOnClickListener {
            if (this::onChooseAvatarActionListener.isInitialized) {
                onChooseAvatarActionListener.invoke(GALLERY_CHOOSE_ACTION_ID)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (this::mCameraManager.isInitialized) {
            mCameraManager.openCamera()
        }
    }

    override fun onPause() {
        super.onPause()
        if (this::mCameraManager.isInitialized) {
            mCameraManager.closeCamera()
        }
    }
}