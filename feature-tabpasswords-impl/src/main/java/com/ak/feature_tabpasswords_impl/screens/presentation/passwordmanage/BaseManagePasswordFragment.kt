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
import androidx.appcompat.app.AppCompatActivity
import com.ak.base.constants.AppConstants
import com.ak.base.extensions.drawTextInner
import com.ak.base.extensions.getColorCompat
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
import kotlinx.android.synthetic.main.fragment_manage_password.*
import kotlinx.android.synthetic.main.fragment_manage_password.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

abstract class BaseManagePasswordFragment<ManagePresenter : BaseManagePasswordPresenter<*>>
    : BasePasswordsModuleFragment<ManagePresenter>(), IBaseManagePasswordView {

    @Inject
    lateinit var mGalleryManager: IPSGalleryManager

    private lateinit var mAvatarChooserDialog: PhotoChooserBottomSheetDialog

    protected abstract fun getPresenter(): ManagePresenter

    override fun getFragmentLayoutResId() = R.layout.fragment_manage_password

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun initViewBeforePresenterAttach(fragmentView: View) {
        super.initViewBeforePresenterAttach(fragmentView)
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
                getPresenter().onPasswordNameTextChanged(s.toString())
            }
        })
        fragmentView.tietPasswordNameField.filters = arrayOf(
            InputFilter.LengthFilter(AppConstants.PASSWORD_NAME_MAX_LENGTH)
        )

        fragmentView.btnManagePasswordAction.setSafeClickListener {
            managePasswordAction()
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

    override fun displaySuccessPasswordManageAction() {
        navController.popBackStack()
    }

    override fun displayPasswordNameInputError(errorMessage: String) {
        fragmentView.tilPasswordNameLayout.error = errorMessage
    }

    override fun hidePasswordNameInputError() {
        fragmentView.tilPasswordNameLayout.error = null
    }

    override fun displayPasswordContentInputError(errorMessage: String) {
        fragmentView.tilPasswordContentLayout.error = errorMessage
    }

    override fun hidePasswordContentInputError() {
        fragmentView.tilPasswordContentLayout.error = null
    }

    override fun drawTextForPasswordAvatar(text: String) {
        val isTextDrawNeeds = text.isNotEmpty()
        val fillColor = getColorCompat(R.color.colorPrimaryLight)
        val textColor = getColorCompat(R.color.staticColorWhite)
        val textSizeInPx = resources.getDimensionPixelSize(R.dimen.add_avatar_inner_text_size)

        fragmentView.ivPasswordAvatarChooser.drawTextInner(
            requireContext(),
            fillColor,
            textColor,
            textSizeInPx,
            text
        )
        fragmentView.lvAvatarChooserImageDesc.setVisibility(!isTextDrawNeeds)
    }

    override fun displayPasswordAvatarChooserImage(bitmapImage: Bitmap?) {
        getPresenter().onAvatarDisplayStateChanged(true)
        fragmentView.ivPasswordAvatarChooser.setImageBitmap(bitmapImage)
        fragmentView.lvAvatarChooserImageDesc.visibility = View.GONE
    }

    override fun deletePasswordAvatarChooserImage() {
        getPresenter().onAvatarDisplayStateChanged(false)
        getPresenter().onPasswordNameTextChanged(tietPasswordNameField.text.toString())
    }

    override fun dismissPasswordAvatarChooserDialog() {
        if (this::mAvatarChooserDialog.isInitialized) {
            mAvatarChooserDialog.dismiss()
        }
    }

    override fun openGalleryForImagePick() {
        if (this::mGalleryManager.isInitialized) {
            activity?.let {
                mGalleryManager.openGalleryForImagePick(it, this)
            }
        }
    }

    override fun openCameraForImagePick() {
        activity?.let {
            CameraPickImageActivity.startCameraPickActivityForResult(it, this)
        }
    }

    private fun initGalleryManager() {
        mGalleryManager.setOnImagePickedListener(getPresenter()::onGalleryAvatarSelected)
    }

    private fun initToolbar() {
        if (activity != null && activity is AppCompatActivity) {
            (activity as AppCompatActivity).apply {
                val actionBarView = fragmentView.tbManagePasswordBar
                setSupportActionBar(actionBarView)
                supportActionBar?.title = getToolbarTitleText()
                actionBarView.setNavigationOnClickListener {
                    hideKeyboard()
                    navController.popBackStack()
                }
            }
        }
    }

    protected abstract fun getToolbarTitleText(): String

    private fun managePasswordAction() {
        hideKeyboard()
        hidePasswordNameInputError()
        hidePasswordContentInputError()
        getPresenter().onManagePasswordAction(
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
            val filePath = data.getStringExtra(CameraPickImageActivity.PICKED_IMAGE_PATH_KEY_EXTRA)
            getPresenter().onCameraImageSelected(filePath)
        }
    }
}