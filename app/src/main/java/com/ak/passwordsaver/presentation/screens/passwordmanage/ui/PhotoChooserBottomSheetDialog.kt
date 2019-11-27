package com.ak.passwordsaver.presentation.screens.passwordmanage.ui

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.screens.passwordmanage.camera.manager.IPSCameraManager
import com.ak.passwordsaver.presentation.screens.passwordmanage.gallery.manager.IPSGalleryManager
import com.ak.passwordsaver.utils.extensions.getColorCompat
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class PhotoChooserBottomSheetDialog : BottomSheetDialogFragment() {

    companion object {
        private const val BOTTOM_SHEET_DIALOG_TAG = "PhotoChooserBottomSheetDialog"
        const val CAMERA_CHOOSE_ACTION_ID = 1
        const val GALLERY_CHOOSE_ACTION_ID = 2

        fun showDialog(fragmentManager: FragmentManager): PhotoChooserBottomSheetDialog {
            val sheetDialogInstance = PhotoChooserBottomSheetDialog()
            sheetDialogInstance.show(fragmentManager, BOTTOM_SHEET_DIALOG_TAG)
            return sheetDialogInstance
        }
    }

    @Inject
    lateinit var mPSCameraManager: IPSCameraManager
    @Inject
    lateinit var mPSGalleryManager: IPSGalleryManager

    lateinit var onChooseAvatarActionListener: (chooseActionId: Int) -> Unit

    private var mFragmentView: View? = null
    private lateinit var mCameraPreviewView: TextureView

    override fun getTheme() = R.style.PhotoChooserBottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mFragmentView = inflater.inflate(R.layout.layout_photo_chooser_dialog, container, false)
        return mFragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mCameraPreviewView = view.findViewById(R.id.texv_photo_chooser_camera_preview)
        mPSCameraManager.initCameraManager(true, mCameraPreviewView)
        view.findViewById<ImageView>(R.id.iv_photo_chooser_gallery_preview).apply {
            val lastGalleryImage = mPSGalleryManager.getLastGalleryImage()
            if (lastGalleryImage != null) {
                setImageBitmap(lastGalleryImage)
            } else {
                @ColorInt
                val noLastImageColor = context.getColorCompat(R.color.colorBlack)
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
        if (this::mPSCameraManager.isInitialized) {
            mPSCameraManager.openCamera()
        }
    }

    override fun onPause() {
        super.onPause()
        if (this::mPSCameraManager.isInitialized) {
            mPSCameraManager.closeCamera()
        }
    }
}