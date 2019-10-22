package com.ak.passwordsaver.presentation.screens.addnew.ui

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.screens.addnew.camera.PSCameraManager

class PhotoChooserBottomSheetDialog : BottomSheetDialogFragment() {

    companion object {
        private const val BOTTOM_SHEET_DIALOG_TAG = "PhotoChooserBottomSheetDialog"

        fun show(fragmentManager: FragmentManager): PhotoChooserBottomSheetDialog {
            val sheetDialogInstance = PhotoChooserBottomSheetDialog()
            sheetDialogInstance.show(fragmentManager, BOTTOM_SHEET_DIALOG_TAG)
            return sheetDialogInstance
        }
    }

    private var fragmentView: View? = null
    private lateinit var mCameraPreviewView: TextureView
    private lateinit var mCameraPreviewManager: PSCameraManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentView = inflater.inflate(R.layout.layout_photo_chooser_button, container, false)
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mCameraPreviewView = view.findViewById(R.id.iv_photo_chooser_camera_preview)
        context?.let {
            mCameraPreviewManager = PSCameraManager(context!!, true, mCameraPreviewView)
        }
    }

    override fun onResume() {
        super.onResume()
        if (this::mCameraPreviewManager.isInitialized) {
            mCameraPreviewManager.openCamera()
        }
    }

    override fun onPause() {
        super.onPause()
        if (this::mCameraPreviewManager.isInitialized) {
            mCameraPreviewManager.closeCamera()
        }
    }
}