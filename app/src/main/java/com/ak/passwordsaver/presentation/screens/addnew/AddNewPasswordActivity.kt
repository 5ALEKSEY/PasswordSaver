package com.ak.passwordsaver.presentation.screens.addnew

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.design.widget.TextInputLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.text.Editable
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
import com.ak.passwordsaver.presentation.screens.addnew.gallery.PSGalleryManager
import com.ak.passwordsaver.presentation.screens.addnew.ui.PhotoChooserBottomSheetDialog
import com.ak.passwordsaver.utils.bindView
import com.ak.passwordsaver.utils.extensions.drawTextInner
import com.ak.passwordsaver.utils.extensions.hideKeyBoard
import com.arellomobile.mvp.presenter.InjectPresenter
import com.eazypermissions.common.model.PermissionResult
import com.eazypermissions.coroutinespermission.PermissionManager
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.FileNotFoundException


class AddNewPasswordActivity : BasePSFragmentActivity(), IAddNewPasswordView {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, AddNewPasswordActivity::class.java))
        }
    }

    @InjectPresenter
    lateinit var mAddNewPasswordPresenter: AddNewPasswordPresenter
    private lateinit var mAvatarChooserDialog: PhotoChooserBottomSheetDialog
    private lateinit var mGalleryManager: PSGalleryManager

    private val mToolbar: Toolbar by bindView(R.id.tb_add_new_password_bar)
    private val mPasswordNameEditText: EditText by bindView(R.id.tiet_password_name_field)
    private val mPasswordNameInputLayout: TextInputLayout by bindView(R.id.til_password_name_layout)
    private val mPasswordContentEditText: EditText by bindView(R.id.tiet_password_content_field)
    private val mPasswordContentInputLayout: TextInputLayout by bindView(R.id.til_password_content_layout)
    private val mPasswordAvatar: CircleImageView by bindView(R.id.iv_password_avatar_chooser)
    private val mAvatarImageDescView: View by bindView(R.id.ll_avatar_chooser_image_desc)
    private val mSavePasswordActionButton: View by bindView(R.id.btn_save_password_action)

    override fun getScreenLayoutResId() = R.layout.activity_add_new_password

    override fun initViewBeforePresenterAttach() {
        super.initViewBeforePresenterAttach()
        initGalleryManager()
        initToolbar()

        mPasswordContentEditText.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener if (actionId == EditorInfo.IME_ACTION_DONE) {
                onSavePasswordAction()
                true
            } else {
                false
            }
        }

        mPasswordAvatar.setOnClickListener {
            GlobalScope.launch {

                val permissionResult = PermissionManager.requestPermissions(
                    this@AddNewPasswordActivity,
                    AppConstants.AVATAR_PERMISSIONS_REQUEST_CODE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )

                when (permissionResult) {
                    is PermissionResult.PermissionGranted -> {
                        Log.d("Alex_testing", "Granted")
                        dismissPasswordAvatarChooserDialog()
                        mAvatarChooserDialog = PhotoChooserBottomSheetDialog.show(supportFragmentManager)
                        mAvatarChooserDialog.onChooseAvatarActionListener =
                            { avatarChooseActionCode ->
                                when (avatarChooseActionCode) {
                                    PhotoChooserBottomSheetDialog.CAMERA_CHOOSE_ACTION_ID -> {
                                        showShortTimeMessage("Camera pick")
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
                mAddNewPasswordPresenter.onPasswordNameTextChanged(s.toString())
            }
        })

        mSavePasswordActionButton.setOnClickListener {
            onSavePasswordAction()
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
            onSavePasswordAction()
            true
        } else {
            super.onOptionsItemSelected(item)
        }

    override fun displaySuccessPasswordSave() {
        Toast.makeText(this, "Successful save", Toast.LENGTH_SHORT).show()
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
        val fillColor = ContextCompat.getColor(this, R.color.colorPrimary)
        val textColor = ContextCompat.getColor(this, R.color.colorWhite)
        val textSizeInPx = resources.getDimensionPixelSize(R.dimen.add_avatar_inner_text_size)

        mPasswordAvatar.drawTextInner(fillColor, textColor, textSizeInPx, text)
        mAvatarImageDescView.visibility = if (isTextDrawNeeds) View.GONE else View.VISIBLE
    }

    override fun displayPasswordAvatarChooserImage(bitmapImage: Bitmap?) {
        mAddNewPasswordPresenter.onAvatarDisplayStateChanged(true)
        mPasswordAvatar.setImageBitmap(bitmapImage)
        mAvatarImageDescView.visibility = View.GONE
    }

    override fun deletePasswordAvatarChooserImage() {
        mAddNewPasswordPresenter.onAvatarDisplayStateChanged(false)
        mAddNewPasswordPresenter.onPasswordNameTextChanged(mPasswordNameEditText.text.toString())
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

    }

    private fun initGalleryManager() {
        mGalleryManager = PSGalleryManager(this)
        mGalleryManager.onImagePickedFromGallery = mAddNewPasswordPresenter::onGalleryAvatarSelected
    }

    private fun initToolbar() {
        setSupportActionBar(mToolbar)
        supportActionBar?.title = "Add new password"
        mToolbar.setNavigationOnClickListener { finish() }
    }

    private fun onSavePasswordAction() {
        hideKeyBoard()
        hidePasswordNameInputError()
        hidePasswordContentInputError()
        mAddNewPasswordPresenter.saveNewPassword(
            mPasswordNameEditText.text.toString(),
            mPasswordContentEditText.text.toString()
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (this::mGalleryManager.isInitialized) {
            mGalleryManager.onActivityResult(requestCode, resultCode, data)
        }
    }
}
