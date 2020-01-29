package com.ak.passwordsaver.presentation.screens.passwordmanage.camera

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.base.constants.AppConstants
import com.ak.passwordsaver.presentation.base.ui.BasePSFragmentActivity
import com.ak.passwordsaver.presentation.screens.passwordmanage.camera.manager.IPSCameraManager
import com.ak.passwordsaver.utils.extensions.getColorCompat
import com.ak.passwordsaver.utils.extensions.setSafeClickListener
import com.ak.passwordsaver.utils.extensions.setVisibility
import com.ak.passwordsaver.utils.extensions.setVisibilityInvisible
import kotlinx.android.synthetic.main.activity_camera_pick_image.*
import moxy.presenter.InjectPresenter
import javax.inject.Inject

class CameraPickImageActivity : BasePSFragmentActivity<CameraPickImagePresenter>(),
    ICameraPickImageView {

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

    override fun getScreenLayoutResId() = R.layout.activity_camera_pick_image

    override fun onBackPressed() {
        sendCancelResult()
    }

    override fun initViewBeforePresenterAttach() {
        super.initViewBeforePresenterAttach()
        initWindow()

        mPSCameraManager.initCameraManager(false, texvCameraPickImagePreview)

        btnTakeImageAction.setSafeClickListener {
            mPSCameraManager.takeImage {
                runOnUiThread { mCameraPickImagePresenter.onImagePicked(it) }
            }
        }

        ivCameraPickImageCancelAction.setSafeClickListener {
            sendCancelResult()
        }

        displayTakeImageStrategy()

        ivRemovePickedImagePanelAction.setSafeClickListener {
            mCameraPickImagePresenter.onPickedImageRemoved()
        }
        ivChooseImagePanelAction.setSafeClickListener {
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
        btnTakeImageAction.setVisibilityInvisible(false)
        ivPreviewImage.setVisibility(true)
        ivPreviewImage.setImageBitmap(previewBitmap)
        ivRemovePickedImagePanelAction.setVisibility(true)
        ivChooseImagePanelAction.setVisibility(true)
        mPSCameraManager.closeCamera()
    }

    override fun displayTakeImageStrategy() {
        btnTakeImageAction.setVisibility(true)
        ivPreviewImage.setVisibility(false)
        ivPreviewImage.setImageBitmap(null)
        ivRemovePickedImagePanelAction.setVisibility(false)
        ivChooseImagePanelAction.setVisibility(false)
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
