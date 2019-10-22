package com.ak.passwordsaver.presentation.screens.addnew.ui

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.FragmentManager
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import com.ak.passwordsaver.R

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
    private lateinit var mCameraManager: CameraManager
    private lateinit var mCameraPreviewService: CameraPreviewService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCameraManager = context?.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }

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
        initCameraPreviewService()
    }

    override fun onResume() {
        super.onResume()
        if (this::mCameraPreviewService.isInitialized) {
            mCameraPreviewService.initCameraPreview()
        }
    }

    override fun onPause() {
        super.onPause()
        if (this::mCameraPreviewService.isInitialized) {
            mCameraPreviewService.closeCameraPreview()
        }
    }

    private fun initCameraPreviewService() {
        if (!this::mCameraManager.isInitialized || context == null) {
            Log.d("Alex_tester", "Camera manager not initialized")
            return
        }

        try {
            mCameraManager.cameraIdList.forEach {
                val characteristics = mCameraManager.getCameraCharacteristics(it)
                val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
                if (facing == CameraCharacteristics.LENS_FACING_BACK) {
                    mCameraPreviewService = CameraPreviewService(
                        context!!,
                        it,
                        mCameraManager,
                        mCameraPreviewView
                    )
                }
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        } finally {
            if (!this::mCameraPreviewService.isInitialized) {
                return
            }
        }
        mCameraPreviewService.initCameraPreview()
    }
}