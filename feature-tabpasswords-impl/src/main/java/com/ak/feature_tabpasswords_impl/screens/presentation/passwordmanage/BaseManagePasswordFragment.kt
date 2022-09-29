package com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.CustomThemeManager
import com.ak.base.constants.AppConstants
import com.ak.base.extensions.applyForLaidOut
import com.ak.base.extensions.drawTextInner
import com.ak.base.extensions.hideKeyboard
import com.ak.base.extensions.setSafeClickListener
import com.ak.base.extensions.setVisibility
import com.ak.feature_tabpasswords_impl.R
import com.ak.feature_tabpasswords_impl.screens.presentation.base.BasePasswordsModuleFragment
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.camera.CameraPickImageActivity
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.gallery.manager.IPSGalleryManager
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.ui.PhotoChooserBottomSheetDialog
import com.eazypermissions.common.model.PermissionResult
import com.eazypermissions.coroutinespermission.PermissionManager
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_manage_password.tietPasswordNameField
import kotlinx.android.synthetic.main.fragment_manage_password.view.btnManagePasswordAction
import kotlinx.android.synthetic.main.fragment_manage_password.view.ivPasswordAvatarChooser
import kotlinx.android.synthetic.main.fragment_manage_password.view.lvAvatarChooserImageDesc
import kotlinx.android.synthetic.main.fragment_manage_password.view.tietPasswordContentField
import kotlinx.android.synthetic.main.fragment_manage_password.view.tietPasswordNameField
import kotlinx.android.synthetic.main.fragment_manage_password.view.tilPasswordContentLayout
import kotlinx.android.synthetic.main.fragment_manage_password.view.tilPasswordNameLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

abstract class BaseManagePasswordFragment<ManageVM : BaseManagePasswordViewModel>
    : BasePasswordsModuleFragment<ManageVM>() {

    @Inject
    lateinit var mGalleryManager: IPSGalleryManager

    private lateinit var mAvatarChooserDialog: PhotoChooserBottomSheetDialog

    override fun getFragmentLayoutResId() = R.layout.fragment_manage_password

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun initView(fragmentView: View) {
        super.initView(fragmentView)
        initGalleryManager()
        initToolbar()

        fragmentView.tietPasswordContentField.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener if (actionId == EditorInfo.IME_ACTION_DONE) {
                managePasswordAction()
                true
            } else {
                false
            }
        }
        fragmentView.tietPasswordContentField.filters = arrayOf(
            InputFilter.LengthFilter(AppConstants.PASSWORD_CONTENT_MAX_LENGTH)
        )

        fragmentView.ivPasswordAvatarChooser.setSafeClickListener {
            GlobalScope.launch {

                val permissionResult = PermissionManager.requestPermissions(
                    this@BaseManagePasswordFragment,
                    AppConstants.AVATAR_PERMISSIONS_REQUEST_CODE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )

                when (permissionResult) {
                    is PermissionResult.PermissionGranted -> {
                        Log.d("Alex_testing", "Granted")
                        dismissPasswordAvatarChooserDialog()
                        mAvatarChooserDialog =
                            PhotoChooserBottomSheetDialog.showDialog(childFragmentManager)
                        mAvatarChooserDialog.onChooseAvatarActionListener =
                            { avatarChooseActionCode ->
                                when (avatarChooseActionCode) {
                                    PhotoChooserBottomSheetDialog.CAMERA_CHOOSE_ACTION_ID -> {
                                        openCameraForImagePick()
                                    }
                                    PhotoChooserBottomSheetDialog.GALLERY_CHOOSE_ACTION_ID -> {
                                        openGalleryForImagePick()
                                    }
                                }
                                dismissPasswordAvatarChooserDialog()
                            }
                    }
                    is PermissionResult.PermissionDenied -> {
                        Log.d("Alex_testing", "PermissionDenied")
                    }
                    is PermissionResult.PermissionDeniedPermanently -> {
                        Log.d("Alex_testing", "PermissionDeniedPermanently")
                    }
                    is PermissionResult.ShowRational -> {
                        Log.d("Alex_testing", "ShowRational")
                    }
                }

            }
        }

        fragmentView.tietPasswordNameField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onPasswordNameTextChanged(s.toString())
            }
        })
        fragmentView.tietPasswordNameField.filters = arrayOf(
            InputFilter.LengthFilter(AppConstants.PASSWORD_NAME_MAX_LENGTH)
        )

        fragmentView.btnManagePasswordAction.setSafeClickListener {
            managePasswordAction()
        }

        // Theme
        addThemedView(fragmentView.tilPasswordNameLayout)
        addThemedView(fragmentView.tilPasswordContentLayout)
    }

    override fun subscriberToViewModel(viewModel: ManageVM) {
        super.subscriberToViewModel(viewModel)
        viewModel.subscribeToSuccessPasswordManage().observe(viewLifecycleOwner) {
            navController.popBackStack()
        }
        viewModel.subscribeToNameInputError().observe(viewLifecycleOwner) { errorMessage ->
            fragmentView.tilPasswordNameLayout.error = errorMessage
        }
        viewModel.subscribeToContentInputError().observe(viewLifecycleOwner) { errorMessage ->
            fragmentView.tilPasswordContentLayout.error = errorMessage
        }
        viewModel.subscribeToAvatarText().observe(viewLifecycleOwner, this::drawTextForPasswordAvatar)
        viewModel.subscribeToAvatarChooserImage().observe(viewLifecycleOwner) { image ->
            if (image != null) {
                displayPasswordAvatarChooserImage(image)
            } else {
                deletePasswordAvatarChooserImage()
            }
        }

    }

    override fun onPause() {
        super.onPause()
        dismissPasswordAvatarChooserDialog()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_new_password_menu, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        if (item.itemId == R.id.action_save_password) {
            managePasswordAction()
            true
        } else {
            super.onOptionsItemSelected(item)
        }

    override fun applyTheme(theme: CustomTheme) {
        super.applyTheme(theme)
        drawTextForPasswordAvatar(viewModel.subscribeToAvatarText().value ?: "", theme)
    }

    private fun drawTextForPasswordAvatar(text: String, theme: CustomTheme = CustomThemeManager.getInstance().getAppliedTheme()) {
        val isTextDrawNeeds = text.isNotEmpty()
        val fillColor = theme.getColor(R.attr.themedPrimaryLightColor)
        val textColor = theme.getColor(R.attr.staticColorWhite)
        val textSizeInPx = resources.getDimensionPixelSize(R.dimen.add_avatar_inner_text_size)

        fragmentView.ivPasswordAvatarChooser.applyForLaidOut {
            drawTextInner(
                requireContext(),
                fillColor,
                textColor,
                textSizeInPx,
                text
            )
            borderColor = theme.getColor(R.attr.themedPrimaryColor)
        }

        fragmentView.lvAvatarChooserImageDesc.applyForLaidOut { setVisibility(!isTextDrawNeeds) }
    }

    private fun displayPasswordAvatarChooserImage(bitmapImage: Bitmap?) {
        viewModel.onAvatarDisplayStateChanged(true)
        fragmentView.ivPasswordAvatarChooser.setImageBitmap(bitmapImage)
        fragmentView.lvAvatarChooserImageDesc.visibility = View.GONE
    }

    private fun deletePasswordAvatarChooserImage() {
        viewModel.onAvatarDisplayStateChanged(false)
        viewModel.onPasswordNameTextChanged(tietPasswordNameField.text.toString())
    }

    private fun dismissPasswordAvatarChooserDialog() {
        if (this::mAvatarChooserDialog.isInitialized) {
            mAvatarChooserDialog.dismiss()
        }
    }

    private fun openGalleryForImagePick() {
        if (this::mGalleryManager.isInitialized) {
            activity?.let {
                mGalleryManager.openGalleryForImagePick(it, this)
            }
        }
    }

    private fun openCameraForImagePick() {
        activity?.let {
            CameraPickImageActivity.startCameraPickActivityForResult(it, this)
        }
    }

    private fun initGalleryManager() {
        mGalleryManager.setOnImagePickedListener(viewModel::onGalleryAvatarSelected)
    }

    private fun initToolbar() {
        applyForToolbarController {
            setToolbarTitle(getToolbarTitleText())
            setupBackAction(R.drawable.ic_back_action) {
                hideKeyboard()
                navController.popBackStack()
            }
        }
    }

    protected abstract fun getToolbarTitleText(): String

    private fun managePasswordAction() {
        hideKeyboard()
        viewModel.onManagePasswordAction(
            fragmentView.tietPasswordNameField.text.toString(),
            fragmentView.tietPasswordContentField.text.toString()
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (this::mGalleryManager.isInitialized) {
            mGalleryManager.onActivityResult(requestCode, resultCode, data)
        }
        if (requestCode == AppConstants.CAMERA_IMAGE_PICK_REQUEST_CODE
            && resultCode == Activity.RESULT_OK
            && data != null
            && data.hasExtra(CameraPickImageActivity.PICKED_IMAGE_PATH_KEY_EXTRA)
        ) {
            data.getStringExtra(CameraPickImageActivity.PICKED_IMAGE_PATH_KEY_EXTRA)?.let {
                viewModel.onCameraImageSelected(it)
            }
        }
    }
}