package com.ak.passwordsaver.presentation.screens.addnew

import android.content.Context
import android.content.Intent
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.base.ui.BasePSFragmentActivity
import com.ak.passwordsaver.presentation.screens.addnew.ui.PhotoChooserBottomSheetDialog
import com.ak.passwordsaver.utils.bindView
import com.ak.passwordsaver.utils.extensions.hideKeyBoard
import com.arellomobile.mvp.presenter.InjectPresenter

class AddNewPasswordActivity : BasePSFragmentActivity(), IAddNewPasswordView {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, AddNewPasswordActivity::class.java))
        }
    }

    @InjectPresenter
    lateinit var mAddNewPasswordPresenter: AddNewPasswordPresenter
    lateinit var mAvatarChooserDialog: PhotoChooserBottomSheetDialog

    private val mToolbar: Toolbar by bindView(R.id.tb_add_new_password_bar)
    private val mPasswordNameEditText: EditText by bindView(R.id.tiet_password_name_field)
    private val mPasswordNameInputLayout: TextInputLayout by bindView(R.id.til_password_name_layout)
    private val mPasswordContentEditText: EditText by bindView(R.id.tiet_password_content_field)
    private val mPasswordContentInputLayout: TextInputLayout by bindView(R.id.til_password_content_layout)
    private val mChooseAvatarAction: Button by bindView(R.id.btn_password_avatar_choose_action)
    private val mPasswordAvatar: ImageView by bindView(R.id.iv_password_avatar_chooser)

    override fun getScreenLayoutResId() = R.layout.activity_add_new_password

    override fun onPause() {
        super.onPause()
        dismissPasswordAvatarChooserDialog()
    }

    override fun initViewBeforePresenterAttach() {
        super.initViewBeforePresenterAttach()
        initToolbar()

        mPasswordContentEditText.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener if (actionId == EditorInfo.IME_ACTION_DONE) {
                onSavePasswordAction()
                true
            } else {
                false
            }
        }

        mChooseAvatarAction.setOnClickListener {
            dismissPasswordAvatarChooserDialog()
            mAvatarChooserDialog = PhotoChooserBottomSheetDialog.show(supportFragmentManager)
        }
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

    override fun displayPasswordAvatarChooserImage(resId: Int) {

    }

    override fun dismissPasswordAvatarChooserDialog() {
        if (this::mAvatarChooserDialog.isInitialized) {
            mAvatarChooserDialog.dismiss()
        }
    }

    private fun initToolbar() {
        setSupportActionBar(mToolbar)
        supportActionBar?.title = "Add new password"
        mToolbar.setNavigationIcon(R.drawable.ic_back_action)
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
}
