package com.ak.passwordsaver.presentation.screens.passwords

import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.widget.Toast
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.base.BasePSFragment
import com.ak.passwordsaver.presentation.screens.addnew.AddNewPasswordActivity
import com.ak.passwordsaver.presentation.screens.passwords.adapter.PasswordItemModel
import com.ak.passwordsaver.presentation.screens.passwords.adapter.PasswordsListRecyclerAdapter
import com.ak.passwordsaver.utils.bindView
import com.arellomobile.mvp.presenter.InjectPresenter


class PasswordsListFragment : BasePSFragment(), IPasswordsListView {

    companion object {
        fun getInstance() = PasswordsListFragment()
    }

    @InjectPresenter
    lateinit var mPasswordsListPresenter: PasswordsListPresenter

    private val mToolbar: Toolbar by bindView(R.id.tb_passwords_list_bar)
    private val mPasswordsRecyclerView: RecyclerView by bindView(R.id.rv_passwords_list)
    private val mAddNewPasswordButton: FloatingActionButton by bindView(R.id.fab_add_new_password_action)
    private lateinit var mPasswordsAdapter: PasswordsListRecyclerAdapter

    override fun getFragmentLayoutResId() = R.layout.fragment_passwords_list

    override fun initViewBeforePresenterAttach() {
        super.initViewBeforePresenterAttach()
        initRecyclerView()
        initToolbar()

        mAddNewPasswordButton.setOnClickListener {
            if (context != null) {
                AddNewPasswordActivity.startActivity(context!!)
            }
        }
    }

    override fun displayPasswords(passwordModelsList: List<PasswordItemModel>) {
        mPasswordsAdapter.insertData(passwordModelsList)
    }

    override fun displayEmptyPasswordsState() {
        Toast.makeText(context, "Passwords are empty", Toast.LENGTH_SHORT).show()
    }

    override fun openPasswordDialogMode(passwordName: String, passwordContent: String) {
        openPasswordToastMode(passwordName, passwordContent)
    }

    override fun openPasswordToastMode(passwordName: String, passwordContent: String) {
        val message = "$passwordName: $passwordContent"
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun openPasswordInCardMode(passwordId: Long) {
        mPasswordsAdapter.openPasswordForPasswordItemId(passwordId)
    }

    private fun initToolbar() {
        if (activity is AppCompatActivity) {
            val appCompatActivity = activity as AppCompatActivity
            appCompatActivity.setSupportActionBar(mToolbar)
            appCompatActivity.supportActionBar?.title = "Passwords list"
        }
    }

    private fun initRecyclerView() {
        mPasswordsAdapter = PasswordsListRecyclerAdapter(mPasswordsListPresenter::passwordShowActionRequired)
        mPasswordsRecyclerView.adapter = mPasswordsAdapter

        mPasswordsRecyclerView.layoutManager = GridLayoutManager(
            context,
            2,
            GridLayoutManager.VERTICAL,
            false
        )

        mPasswordsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    mAddNewPasswordButton.hide()
                } else {
                    mAddNewPasswordButton.show()
                }

                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }
}