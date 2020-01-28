package com.ak.passwordsaver.presentation.screens.passwordmanage

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.base.constants.AppConstants
import com.ak.passwordsaver.presentation.base.ui.BasePSFragmentActivity
import com.ak.passwordsaver.presentation.screens.passwordmanage.camera.CameraPickImageActivity
import com.ak.passwordsaver.presentation.screens.passwordmanage.gallery.manager.IPSGalleryManager
import com.ak.passwordsaver.presentation.screens.passwordmanage.ui.PhotoChooserBottomSheetDialog
import com.ak.passwordsaver.utils.extensions.drawTextInner
import com.ak.passwordsaver.utils.extensions.getColorCompat
import com.ak.passwordsaver.utils.extensions.hideKeyBoard
import com.ak.passwordsaver.utils.extensions.setSafeClickListener
import com.ak.passwordsaver.utils.extensions.setVisibility
import com.eazypermissions.common.model.PermissionResult
import com.eazypermissions.coroutinespermission.PermissionManager
import kotlinx.android.synthetic.main.activity_manage_password.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseManagePasswordActivity<ManagePresenter : BaseManagePasswordPresenter<*>>
    : BasePSFragmentActivity(), IBaseManagePasswordView {

    @Inject
    lateinit var mGalleryManager: IPSGalleryManager
    private lateinit var mAvatarChooserDialog: PhotoChooserBottomSheetDialog

    protected abstract fun getPresenter(): ManagePresenter

    override fun getScreenLayoutResId() = R.layout.activity_manage_password

    override fun initViewBeforePresenterAttach() {
        super.initViewBeforePresenterAttach()
        initGalleryManager()
        initToolbar()

        tietPasswordContentField.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener if (actionId == EditorInfo.IME_ACTION_DONE) {
                managePasswordAction()
                true
            } else {
                false
            }
        }
        tietPasswordContentField.filters = arrayOf(
            InputFilter.LengthFilter(AppConstants.PASSWORD_CONTENT_MAX_LENGTH)
        )

        ivPasswordAvatarChooser.setSafeClickListener {
            GlobalScope.launch {

                val permissionResult = PermissionManager.requestPermissions(
                    this@BaseManagePasswordActivity,
                    AppConstants.AVATAR_PERMISSIONS_REQUEST_CODE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )

                when (permissionResult) {
                    is PermissionResult.PermissionGranted -> {
                        Log.d("Alex_testing", "Granted")
                        dismissPasswordAvatarChooserDialog()
                        mAvatarChooserDialog =
                            PhotoChooserBottomSheetDialog.showDialog(supportFragmentManager)
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

        tietPasswordNameField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                getPresenter().onPasswordNameTextChanged(s.toString())
            }
        })
        tietPasswordNameField.filters = arrayOf(
            InputFilter.LengthFilter(AppConstants.PASSWORD_NAME_MAX_LENGTH)
        )

        btnManagePasswordAction.setSafeClickListener {
            managePasswordAction()
        }
    }

    override fun onPause() {
        super.onPause()
        dismissPasswordAvatarChooserDialog()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_new_password_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?) =
        if (item != null && item.itemId == R.id.action_save_password) {
            managePasswordAction()
            true
        } else {
            super.onOptionsItemSelected(item)
        }

    override fun displaySuccessPasswordManageAction() {
        Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun displayPasswordNameInputError(errorMessage: String) {
        tilPasswordNameLayout.error = errorMessage
    }

    override fun hidePasswordNameInputError() {
        tilPasswordNameLayout.error = null
    }

    override fun displayPasswordContentInputError(errorMessage: String) {
        tilPasswordContentLayout.error = errorMessage
    }

    override fun hidePasswordContentInputError() {
        tilPasswordContentLayout.error = null
    }

    override fun drawTextForPasswordAvatar(text: String) {
        val isTextDrawNeeds = text.isNotEmpty()
        val fillColor = getColorCompat(R.color.colorPrimary)
        val textColor = getColorCompat(R.color.colorWhite)
        val textSizeInPx = resources.getDimensionPixelSize(R.dimen.add_avatar_inner_text_size)

        ivPasswordAvatarChooser.drawTextInner(fillColor, textColor, textSizeInPx, text)
        lvAvatarChooserImageDesc.setVisibility(!isTextDrawNeeds)
    }

    override fun displayPasswordAvatarChooserImage(bitmapImage: Bitmap?) {
        getPresenter().onAvatarDisplayStateChanged(true)
        ivPasswordAvatarChooser.setImageBitmap(bitmapImage)
        lvAvatarChooserImageDesc.visibility = View.GONE
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
            mGalleryManager.openGalleryForImagePick(this)
        }
    }

    override fun openCameraForImagePick() {
        CameraPickImageActivity.startCameraPickActivityForResult(
            this@BaseManagePasswordActivity
        )
    }

    private fun initGalleryManager() {
        mGalleryManager.setOnImagePickedListener(getPresenter()::onGalleryAvatarSelected)
    }

    private fun initToolbar() {
        setSupportActionBar(tbManagePasswordBar)
        supportActionBar?.title = getToolbarTitleText()
        tbManagePasswordBar.setNavigationOnClickListener { finish() }
    }

    protected abstract fun getToolbarTitleText(): String

    private fun managePasswordAction() {
        hideKeyBoard()
        hidePasswordNameInputError()
        hidePasswordContentInputError()
        getPresenter().onManagePasswordAction(
            tietPasswordNameField.text.toString(),
            tietPasswordContentField.text.toString()
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