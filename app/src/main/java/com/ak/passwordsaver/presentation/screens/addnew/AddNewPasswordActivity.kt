package com.ak.passwordsaver.presentation.screens.addnew

import android.content.Context
import android.content.Intent
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.base.BasePSFragmentActivity
import com.ak.passwordsaver.utils.bindView
import com.arellomobile.mvp.presenter.InjectPresenter

class AddNewPasswordActivity : BasePSFragmentActivity(), IAddNewPasswordView {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, AddNewPasswordActivity::class.java))
        }
    }

    @InjectPresenter
    lateinit var mAddNewPasswordPresenter: AddNewPasswordPresenter

    private val mToolbar: Toolbar by bindView(R.id.tb_add_new_password_bar)

    override fun getScreenLayoutResId() = R.layout.activity_add_new_password

    override fun initViewBeforePresenterAttach() {
        super.initViewBeforePresenterAttach()
        initToolbar()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_new_password_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?) =
        if (item != null && item.itemId == R.id.action_save_password) {
//            mAddNewPasswordPresenter.chetoTam()
            true
        } else {
            super.onOptionsItemSelected(item)
        }

    private fun initToolbar() {
        setSupportActionBar(mToolbar)
        supportActionBar?.title = "Add new password"
        mToolbar.setNavigationIcon(R.drawable.ic_back_action)
        mToolbar.setNavigationOnClickListener { finish() }
    }
}
