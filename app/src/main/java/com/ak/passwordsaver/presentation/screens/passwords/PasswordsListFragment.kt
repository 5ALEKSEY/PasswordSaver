package com.ak.passwordsaver.presentation.screens.passwords

import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.adapter.passwords.PasswordItemModel
import com.ak.passwordsaver.presentation.adapter.passwords.PasswordsListRecyclerAdapter
import com.ak.passwordsaver.presentation.base.BasePSFragment
import com.ak.passwordsaver.presentation.screens.addnew.AddNewPasswordActivity
import com.ak.passwordsaver.utils.bindView
import com.arellomobile.mvp.presenter.InjectPresenter


class PasswordsListFragment : BasePSFragment(), IPasswordsListView {

    companion object {
        fun getInstance() = PasswordsListFragment()
    }

    @InjectPresenter
    lateinit var mPasswordsListPresenter: PasswordsListPresenter

    private val mPasswordsRecyclerView: RecyclerView by bindView(R.id.rv_passwords_list)
    private val mAddNewPasswordButton: FloatingActionButton by bindView(R.id.fab_add_new_password_action)
    private lateinit var mPasswordsAdapter: PasswordsListRecyclerAdapter

    override fun getFragmentLayoutResId() = R.layout.fragment_passwords_list

    override fun initViewBeforePresenterAttach() {
        super.initViewBeforePresenterAttach()
        initRecyclerView()

        mAddNewPasswordButton.setOnClickListener {
            if (context != null) {
                AddNewPasswordActivity.startActivity(context!!)
            }
        }
    }

    private fun initRecyclerView() {
        mPasswordsAdapter = PasswordsListRecyclerAdapter()
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

    override fun displayPasswords(passwordModelsList: List<PasswordItemModel>) {
        mPasswordsAdapter.insertData(passwordModelsList)
    }

    override fun displayEmptyPasswordsState() {
        Toast.makeText(context, "Passwords are empty", Toast.LENGTH_SHORT).show()
    }
}