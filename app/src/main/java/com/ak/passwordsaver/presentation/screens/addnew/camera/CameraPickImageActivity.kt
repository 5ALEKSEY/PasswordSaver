package com.ak.passwordsaver.presentation.screens.addnew.camera

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.TextureView
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.base.constants.AppConstants
import com.ak.passwordsaver.presentation.base.ui.BasePSFragmentActivity
import com.ak.passwordsaver.presentation.screens.addnew.camera.manager.IPSCameraManager
import com.ak.passwordsaver.utils.bindView
import com.ak.passwordsaver.utils.extensions.getColorCompat
import com.ak.passwordsaver.utils.extensions.setVisibility
import com.ak.passwordsaver.utils.extensions.setVisibilityInvisible
import com.arellomobile.mvp.presenter.InjectPresenter
import javax.inject.Inject

class CameraPickImageActivity : BasePSFragmentActivity(), ICameraPickImageView {

    companion object {
        const val PICKED_IMAGE_PATH_KEY_EXTRA = "picked_image_path"

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

    @InjectPresenter
    lateinit var mCameraPickImagePresenter: CameraPickImagePresenter
    @Inject
    lateinit var mPSCameraManager: IPSCameraManager

    private val mCameraPreviewView: TextureView by bindView(R.id.texv_camera_pick_image_preview)
    private val mCancelPickButton: View by bindView(R.id.iv_camera_pick_image_cancel_action)
    private val mTakeImageButton: View by bindView(R.id.btn_take_image_action)
    private val mPreviewImage: ImageView by bindView(R.id.iv_preview_image)
    private val mRemovePickedImagePanelButton: View by bindView(R.id.iv_remove_picked_image_panel_action)
    private val mChooseImagePanelButton: View by bindView(R.id.iv_choose_image_panel_action)

    override fun getScreenLayoutResId() = R.layout.activity_camera_pick_image

    override fun onBackPressed() {
        sendCancelResult()
    }

    override fun initViewBeforePresenterAttach() {
        super.initViewBeforePresenterAttach()
        initWindow()

        mPSCameraManager.initCameraManager(false, mCameraPreviewView)

        mTakeImageButton.setOnClickListener {
            mPSCameraManager.takeImage {
                runOnUiThread { mCameraPickImagePresenter.onImagePicked(it) }
            }
        }

        mCancelPickButton.setOnClickListener {
            sendCancelResult()
        }

        displayTakeImageStrategy()

        mRemovePickedImagePanelButton.setOnClickListener {
            mCameraPickImagePresenter.onPickedImageRemoved()
        }
        mChooseImagePanelButton.setOnClickListener {
            mCameraPickImagePresenter.savePickedImageAndFinish()
        }
    }

    private fun initWindow() {
        window?.apply {
            addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)  // hide status bar
            navigationBarColor = getColorCompat(R.color.colorBlack)
        }
    }

    override fun onResume() {
        super.onResume()
        mPSCameraManager.openCamera()
    }

    override fun onPause() {
        super.onPause()
        mPSCameraManager.closeCamera()
    }

    override fun displayPreviewImageStrategy(previewBitmap: Bitmap) {
        mTakeImageButton.setVisibilityInvisible(false)
        mPreviewImage.setVisibility(true)
        mPreviewImage.setImageBitmap(previewBitmap)
        mRemovePickedImagePanelButton.setVisibility(true)
        mChooseImagePanelButton.setVisibility(true)
        mPSCameraManager.closeCamera()
    }

    override fun displayTakeImageStrategy() {
        mTakeImageButton.setVisibility(true)
        mPreviewImage.setVisibility(false)
        mPreviewImage.setImageBitmap(null)
        mRemovePickedImagePanelButton.setVisibility(false)
        mChooseImagePanelButton.setVisibility(false)
        mPSCameraManager.openCamera()
    }

    override fun sendSuccessImagePickResult(filePath: String) {
        val resultIntent = Intent()
        resultIntent.putExtra(PICKED_IMAGE_PATH_KEY_EXTRA, filePath)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    override fun sendCancelResult() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }
}
