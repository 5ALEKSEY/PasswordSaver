package com.ak.passwordsaver.presentation.screens.addnew.camera

import android.app.Activity
import android.content.Intent
import android.support.annotation.ColorRes
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.view.TextureView
import android.view.View
import android.view.WindowManager
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

    private val mRootConstraintLayout: ConstraintLayout by bindView(R.id.cl_camera_pick_image_root_layout)
    private val mCameraPreviewView: TextureView by bindView(R.id.texv_camera_pick_image_preview)
    private val mCameraActionsPanel: View by bindView(R.id.cl_camera_actions_panel_container)
    private val mCancelPickButton: View by bindView(R.id.iv_camera_pick_image_cancel_action)
    private val mSwitchCameraButton: Button by bindView(R.id.btn_camera_pick_image_switch_camera_action)

    private var mPSCameraManager: PSCameraManager? = null
    private var mIsFacingBackState = true

    override fun getScreenLayoutResId() = R.layout.activity_camera_pick_image

    override fun initViewBeforePresenterAttach() {
        super.initViewBeforePresenterAttach()
        // hide status bar
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        mPSCameraManager = PSCameraManager(this, true, mCameraPreviewView)

        mSwitchCameraButton.visibility = if (mPSCameraManager?.isFacingFrontCameraExist == true) {
            View.VISIBLE
        } else {
            View.GONE
        }
        mSwitchCameraButton.setOnClickListener {
            switchCamera(!mIsFacingBackState)
        }

        mCancelPickButton.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
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

    override fun switchCamera(isFacingBackState: Boolean) {
        @ColorRes
        val cameraActionsPanelColor = if (isFacingBackState) {
            R.color.camera_pick_image_back_facing_panel_background
        } else {
            R.color.camera_pick_image_front_facing_panel_background
        }
        mCameraActionsPanel.setBackgroundColor(
            ContextCompat.getColor(
                this,
                cameraActionsPanelColor
            )
        )

        ConstraintSet().apply {
            clone(mRootConstraintLayout)
            if (isFacingBackState) {
                connect(
                    mCameraPreviewView.id, ConstraintSet.BOTTOM,
                    mCameraActionsPanel.id, ConstraintSet.TOP
                )
            } else {
                connect(
                    mCameraPreviewView.id, ConstraintSet.BOTTOM,
                    ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM
                )
            }
            applyTo(mRootConstraintLayout)
        }

        mPSCameraManager?.switchCamera()

        mIsFacingBackState = isFacingBackState
    }
}
