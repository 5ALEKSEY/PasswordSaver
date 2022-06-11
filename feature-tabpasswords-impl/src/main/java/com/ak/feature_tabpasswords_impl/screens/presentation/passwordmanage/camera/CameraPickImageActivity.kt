package com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.camera

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.ak.app_theme.theme.uicomponents.BaseThemeActivity
import com.ak.base.constants.AppConstants
import com.ak.base.extensions.getColorCompat
import com.ak.base.extensions.setSafeClickListener
import com.ak.base.extensions.setVisibility
import com.ak.base.extensions.setVisibilityInvisible
import com.ak.base.extensions.showToastMessage
import com.ak.base.viewmodel.injectViewModel
import com.ak.feature_tabpasswords_impl.R
import com.ak.feature_tabpasswords_impl.di.FeatureTabPasswordsComponentInitializer
import com.ak.feature_tabpasswords_impl.di.modules.TabPasswordsViewModelsModule
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.camera.manager.IPSCameraManager
import javax.inject.Inject
import javax.inject.Named
import kotlinx.android.synthetic.main.activity_camera_pick_image.btnTakeImageAction
import kotlinx.android.synthetic.main.activity_camera_pick_image.ivCameraPickImageCancelAction
import kotlinx.android.synthetic.main.activity_camera_pick_image.ivChooseImagePanelAction
import kotlinx.android.synthetic.main.activity_camera_pick_image.ivPreviewImage
import kotlinx.android.synthetic.main.activity_camera_pick_image.ivRemovePickedImagePanelAction
import kotlinx.android.synthetic.main.activity_camera_pick_image.texvCameraPickImagePreview

class CameraPickImageActivity : BaseThemeActivity() {

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
    @field:Named(TabPasswordsViewModelsModule.PASSWORDS_VIEW_MODELS_FACTORY_KEY)
    protected lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    protected lateinit var cameraManager: IPSCameraManager

    private lateinit var viewModel: CameraPickImageViewModel

    override fun onBackPressed() {
        sendCancelResult()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as? FeatureTabPasswordsComponentInitializer)?.initializeTabPasswordsComponent()?.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_pick_image)
        viewModel = injectViewModel(viewModelFactory)
        initView()
        subscribeToViewModel(viewModel)
    }

    private fun initView() {
        initWindow()
        cameraManager.initCameraManager(false, texvCameraPickImagePreview)

        btnTakeImageAction.setSafeClickListener {
            cameraManager.takeImage {
                runOnUiThread { viewModel.onImagePicked(it) }
            }
        }

        ivCameraPickImageCancelAction.setSafeClickListener {
            sendCancelResult()
        }

        displayTakeImageStrategy()

        ivRemovePickedImagePanelAction.setSafeClickListener {
            viewModel.onPickedImageRemoved()
        }
        ivChooseImagePanelAction.setSafeClickListener {
            viewModel.savePickedImageAndFinish()
        }
    }

    private fun subscribeToViewModel(viewModel: CameraPickImageViewModel) {
        viewModel.subscribeToShortTimeMessageLiveData().observe(this) {
            showToastMessage(it)
        }
        viewModel.subscribeToPreviewImageLiveData().observe(this, this::displayPreviewImageStrategy)
        viewModel.subscribeToTakeImageLiveData().observe(this) {
            displayTakeImageStrategy()
        }
        viewModel.subscribeToImagePickedResultPathLiveData().observe(this, this::sendSuccessImagePickResult)
    }

    private fun initWindow() {
        window?.apply {
            addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)  // hide status bar
            navigationBarColor = getColorCompat(R.color.staticColorBlack)
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

    private fun displayPreviewImageStrategy(previewBitmap: Bitmap) {
        btnTakeImageAction.setVisibilityInvisible(false)
        ivPreviewImage.setVisibility(true)
        ivPreviewImage.setImageBitmap(previewBitmap)
        ivRemovePickedImagePanelAction.setVisibility(true)
        ivChooseImagePanelAction.setVisibility(true)
        cameraManager.closeCamera()
    }

    private fun displayTakeImageStrategy() {
        btnTakeImageAction.setVisibility(true)
        ivPreviewImage.setVisibility(false)
        ivPreviewImage.setImageBitmap(null)
        ivRemovePickedImagePanelAction.setVisibility(false)
        ivChooseImagePanelAction.setVisibility(false)
        cameraManager.openCamera()
    }

    private fun sendSuccessImagePickResult(filePath: String) {
        val resultIntent = Intent()
        resultIntent.putExtra(PICKED_IMAGE_PATH_KEY_EXTRA, filePath)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun sendCancelResult() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }
}
