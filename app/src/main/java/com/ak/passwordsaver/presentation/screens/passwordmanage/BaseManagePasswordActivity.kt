package com.ak.passwordsaver.presentation.screens.passwordmanage

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.base.constants.AppConstants
import com.ak.passwordsaver.presentation.base.ui.BasePSFragmentActivity
import com.ak.passwordsaver.presentation.screens.passwordmanage.camera.CameraPickImageActivity
import com.ak.passwordsaver.presentation.screens.passwordmanage.gallery.manager.IPSGalleryManager
import com.ak.passwordsaver.presentation.screens.passwordmanage.ui.PhotoChooserBottomSheetDialog
import com.ak.passwordsaver.utils.bindView
import com.ak.passwordsaver.utils.extensions.drawTextInner
import com.ak.passwordsaver.utils.extensions.getColorCompat
import com.ak.passwordsaver.utils.extensions.hideKeyBoard
import com.ak.passwordsaver.utils.extensions.setVisibility
import com.eazypermissions.common.model.PermissionResult
import com.eazypermissions.coroutinespermission.PermissionManager
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseManagePasswordActivity<ManagePresenter : BaseManagePasswordPresenter<*>>
    : BasePSFragmentActivity(), IBaseManagePasswordView {

    @Inject
    lateinit var mGalleryManager: IPSGalleryManager
    private lateinit var mAvatarChooserDialog: PhotoChooserBottomSheetDialog

    protected val mPasswordNameEditText: EditText by bindView(R.id.tiet_password_name_field)
    protected val mPasswordContentEditText: EditText by bindView(R.id.tiet_password_content_field)

    private val mToolbar: Toolbar by bindView(R.id.tb_manage_password_bar)
    private val mPasswordNameInputLayout: TextInputLayout by bindView(R.id.til_password_name_layout)
    private val mPasswordContentInputLayout: TextInputLayout by bindView(R.id.til_password_content_layout)
    private val mPasswordAvatar: CircleImageView by bindView(R.id.iv_password_avatar_chooser)
    private val mAvatarImageDescView: View by bindView(R.id.ll_avatar_chooser_image_desc)
    private val mManagePasswordActionButton: View by bindView(R.id.btn_manage_password_action)

    protected abstract fun getPresenter(): ManagePresenter

    override fun getScreenLayoutResId() = R.layout.activity_manage_password

    override fun initViewBeforePresenterAttach() {
        super.initViewBeforePresenterAttach()
        initGalleryManager()
        initToolbar()

        mPasswordContentEditText.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener if (actionId == EditorInfo.IME_ACTION_DONE) {
                managePasswordAction()
                true
            } else {
                false
            }
        }
        mPasswordContentEditText.filters = arrayOf(
            InputFilter.LengthFilter(AppConstants.PASSWORD_CONTENT_MAX_LENGTH)
        )

        mPasswordAvatar.setOnClickListener {
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

        mPasswordNameEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                getPresenter().onPasswordNameTextChanged(s.toString())
            }
        })
        mPasswordNameEditText.filters = arrayOf(
            InputFilter.LengthFilter(AppConstants.PASSWORD_NAME_MAX_LENGTH)
        )

        mManagePasswordActionButton.setOnClickListener {
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
        mPasswordNameInputLayout.error = errorMessage
    }

    override fun hidePasswordNameInputError() {
        mPasswordNameInputLayout.error = null
    }

    override fun displayPasswordContentInputError(errorMessage: String) {
        mPasswordContentInputLayout.error = errorMessage
    }

    override fun hidePasswordContentInputError() {
        mPasswordContentInputLayout.error = null
    }

    override fun drawTextForPasswordAvatar(text: String) {
        val isTextDrawNeeds = text.isNotEmpty()
        val fillColor = getColorCompat(R.color.colorPrimary)
        val textColor = getColorCompat(R.color.colorWhite)
        val textSizeInPx = resources.getDimensionPixelSize(R.dimen.add_avatar_inner_text_size)

        mPasswordAvatar.drawTextInner(fillColor, textColor, textSizeInPx, text)
        mAvatarImageDescView.setVisibility(!isTextDrawNeeds)
    }

    override fun displayPasswordAvatarChooserImage(bitmapImage: Bitmap?) {
        getPresenter().onAvatarDisplayStateChanged(true)
        mPasswordAvatar.setImageBitmap(bitmapImage)
        mAvatarImageDescView.visibility = View.GONE
    }

    override fun deletePasswordAvatarChooserImage() {
        getPresenter().onAvatarDisplayStateChanged(false)
        getPresenter().onPasswordNameTextChanged(mPasswordNameEditText.text.toString())
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
        setSupportActionBar(mToolbar)
        supportActionBar?.title = getToolbarTitleText()
        mToolbar.setNavigationOnClickListener { finish() }
    }

    protected abstract fun getToolbarTitleText(): String

    private fun managePasswordAction() {
        hideKeyBoard()
        hidePasswordNameInputError()
        hidePasswordContentInputError()
        getPresenter().onManagePasswordAction(
            mPasswordNameEditText.text.toString(),
            mPasswordContentEditText.text.toString()
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