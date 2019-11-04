package com.ak.passwordsaver.presentation.screens.addnew.camera

import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.TextureView
import android.widget.Button
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.base.constants.AppConstants
import com.ak.passwordsaver.presentation.base.ui.BasePSFragmentActivity
import com.ak.passwordsaver.utils.bindView

class CameraPickImageActivity : BasePSFragmentActivity(), ICameraPickImageView {

    companion object {
        fun startCameraPickActivityForResult(context: FragmentActivity, fragment: Fragment) {
            val intent = getCameraPickActivityIntent(context)
            context.startActivityFromFragment(
                fragment,
                intent,
                AppConstants.CAMERA_IMAGE_PICK_REQUEST_CODE
            )
        }

        fun startCameraPickActivityForResult(context: FragmentActivity) {
            val intent = getCameraPickActivityIntent(context)
            context.startActivityForResult(intent, AppConstants.CAMERA_IMAGE_PICK_REQUEST_CODE)
        }

        private fun getCameraPickActivityIntent(context: FragmentActivity) =
            Intent(context, CameraPickImageActivity::class.java)
    }

    private val mCameraPreviewView: TextureView by bindView(R.id.texv_camera_pick_image_preview)
    private val mSwitchCameraButton: Button by bindView(R.id.btn_camera_pick_image_switch_camera_action)

    private var mPSCameraManager: PSCameraManager? = null

    override fun getScreenLayoutResId() = R.layout.activity_camera

    override fun initViewBeforePresenterAttach() {
        super.initViewBeforePresenterAttach()
        mPSCameraManager = PSCameraManager(this, true, mCameraPreviewView)
        mSwitchCameraButton.setOnClickListener {
            mPSCameraManager?.switchCamera()
        }
    }

    override fun onResume() {
        super.onResume()
        mPSCameraManager?.openCamera()
    }

    override fun onPause() {
        super.onPause()
        mPSCameraManager?.closeCamera()
    }
}
