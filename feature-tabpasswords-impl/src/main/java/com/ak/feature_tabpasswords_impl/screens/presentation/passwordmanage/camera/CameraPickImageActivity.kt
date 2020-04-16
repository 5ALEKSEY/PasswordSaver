package com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.camera

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.ak.base.constants.AppConstants
import com.ak.base.extensions.getColorCompat
import com.ak.base.extensions.setSafeClickListener
import com.ak.base.extensions.setVisibility
import com.ak.base.extensions.setVisibilityInvisible
import com.ak.base.extensions.showToastMessage
import com.ak.base.extensions.vibrate
import com.ak.feature_tabpasswords_impl.R
import com.ak.feature_tabpasswords_impl.di.FeatureTabPasswordsComponent
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.camera.manager.IPSCameraManager
import kotlinx.android.synthetic.main.activity_camera_pick_image.*
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class CameraPickImageActivity : MvpAppCompatActivity(), ICameraPickImageView {

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

    @Inject
    lateinit var daggerPresenter: CameraPickImagePresenter

    @InjectPresenter
    lateinit var cameraPickImagePresenter: CameraPickImagePresenter

    @ProvidePresenter
    fun providePresenter(): CameraPickImagePresenter = daggerPresenter

    @Inject
    lateinit var cameraManager: IPSCameraManager

    override fun onBackPressed() {
        sendCancelResult()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        mvpDelegate.onAttach()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        FeatureTabPasswordsComponent.get().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_pick_image)
        initViewBeforePresenterAttach()
        mvpDelegate.onAttach()
    }

    private fun initViewBeforePresenterAttach() {
        initWindow()
        cameraManager.initCameraManager(false, texvCameraPickImagePreview)

        btnTakeImageAction.setSafeClickListener {
            cameraManager.takeImage {
                runOnUiThread { cameraPickImagePresenter.onImagePicked(it) }
            }
        }

        ivCameraPickImageCancelAction.setSafeClickListener {
            sendCancelResult()
        }

        displayTakeImageStrategy()

        ivRemovePickedImagePanelAction.setSafeClickListener {
            cameraPickImagePresenter.onPickedImageRemoved()
        }
        ivChooseImagePanelAction.setSafeClickListener {
            cameraPickImagePresenter.savePickedImageAndFinish()
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
        cameraManager.openCamera()
    }

    override fun onPause() {
        super.onPause()
        cameraManager.closeCamera()
    }

    override fun displayPreviewImageStrategy(previewBitmap: Bitmap) {
        btnTakeImageAction.setVisibilityInvisible(false)
        ivPreviewImage.setVisibility(true)
        ivPreviewImage.setImageBitmap(previewBitmap)
        ivRemovePickedImagePanelAction.setVisibility(true)
        ivChooseImagePanelAction.setVisibility(true)
        cameraManager.closeCamera()
    }

    override fun displayTakeImageStrategy() {
        btnTakeImageAction.setVisibility(true)
        ivPreviewImage.setVisibility(false)
        ivPreviewImage.setImageBitmap(null)
        ivRemovePickedImagePanelAction.setVisibility(false)
        ivChooseImagePanelAction.setVisibility(false)
        cameraManager.openCamera()
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

    override fun showShortTimeMessage(message: String) {
        showToastMessage(message)
    }

    override fun invokeVibration(vibrateDuration: Long) {
        vibrate(vibrateDuration)
    }
}
