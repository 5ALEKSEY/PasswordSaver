package com.ak.passwordsaver.presentation.fragments

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.adapter.passwords.PasswordItemModel
import com.ak.passwordsaver.presentation.adapter.passwords.PasswordsListRecyclerAdapter
import com.ak.passwordsaver.presentation.fragments.base.BasePSFragment
import com.ak.passwordsaver.utils.bindView


class PasswordsListFragment : BasePSFragment() {

    companion object {
        fun getInstance() = PasswordsListFragment()
    }

    private val mPasswordsRecyclerView: RecyclerView by bindView(R.id.rv_passwords_list)
    private val mAddNewPasswordButton: FloatingActionButton by bindView(R.id.fab_add_new_password_action)
    private lateinit var mPasswordsAdapter: PasswordsListRecyclerAdapter

    override fun getFragmentLayoutResId() = R.layout.fragment_passwords_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPasswordsAdapter = PasswordsListRecyclerAdapter()
        mPasswordsRecyclerView.adapter = mPasswordsAdapter
        mPasswordsRecyclerView.layoutManager = GridLayoutManager(
            context,
            2,
            GridLayoutManager.VERTICAL,
            false
        )

        mPasswordsAdapter.insertData(
            arrayListOf(
                PasswordItemModel("1111", "", "34567"),
                PasswordItemModel("2222", "", "464ded65"),
                PasswordItemModel("3333", "", "frfe46"),
                PasswordItemModel("4444", "", "d4erfd684"),
                PasswordItemModel("5555", "", "rrrrrr"),
                PasswordItemModel("6666", "", "erfref"),
                PasswordItemModel("7777", "", "d4erfeeed684"),
                PasswordItemModel("8888", "", "rfvg69"),
                PasswordItemModel("9999", "", "89657"),
                PasswordItemModel("1010", "", "78897frf89898"),
                PasswordItemModel("1110", "", "8757ded8578"),
                PasswordItemModel("34534", "", "56de68")
            )
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