package com.ak.passwordsaver.presentation.screens.passwordmanage

import android.support.design.widget.TextInputLayout
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.EditText
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.base.ui.BasePSFragmentActivity
import com.ak.passwordsaver.utils.bindView
import de.hdodenhof.circleimageview.CircleImageView

abstract class BaseManagePasswordActivity : BasePSFragmentActivity() {

    override fun getScreenLayoutResId()= R.layout.activity_manage_password

    private val mToolbar: Toolbar by bindView(R.id.tb_manage_password_bar)
    private val mPasswordNameEditText: EditText by bindView(R.id.tiet_password_name_field)
    private val mPasswordNameInputLayout: TextInputLayout by bindView(R.id.til_password_name_layout)
    private val mPasswordContentEditText: EditText by bindView(R.id.tiet_password_content_field)
    private val mPasswordContentInputLayout: TextInputLayout by bindView(R.id.til_password_content_layout)
    private val mPasswordAvatar: CircleImageView by bindView(R.id.iv_password_avatar_chooser)
    private val mAvatarImageDescView: View by bindView(R.id.ll_avatar_chooser_image_desc)
    private val mManagePasswordActionButton: View by bindView(R.id.btn_manage_password_action)


}