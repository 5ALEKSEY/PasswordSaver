package com.ak.passwordsaver.presentation.screens.passwords

import android.app.Activity
import android.content.Intent
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.ActionMode
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.base.constants.AppConstants
import com.ak.passwordsaver.presentation.base.ui.BasePSFragment
import com.ak.passwordsaver.presentation.screens.addnew.AddNewPasswordActivity
import com.ak.passwordsaver.presentation.screens.auth.SecurityActivity
import com.ak.passwordsaver.presentation.screens.auth.SecurityPresenter
import com.ak.passwordsaver.presentation.screens.passwords.actionMode.IPasswordsActionModeView
import com.ak.passwordsaver.presentation.screens.passwords.actionMode.PasswordsActionModePresenter
import com.ak.passwordsaver.presentation.screens.passwords.adapter.PasswordItemModel
import com.ak.passwordsaver.presentation.screens.passwords.adapter.PasswordsListRecyclerAdapter
import com.ak.passwordsaver.utils.bindView
import com.arellomobile.mvp.presenter.InjectPresenter


class PasswordsListFragment : BasePSFragment(), IPasswordsListView, IPasswordsActionModeView {

    companion object {
        fun getInstance() = PasswordsListFragment()
    }

    @InjectPresenter
    lateinit var mPasswordsListPresenter: PasswordsListPresenter
    @InjectPresenter
    lateinit var mPasswordsActionModePresenter: PasswordsActionModePresenter

    private val mToolbar: Toolbar by bindView(R.id.tb_passwords_list_bar)
    private val mPasswordsRecyclerView: RecyclerView by bindView(R.id.rv_passwords_list)
    private val mAddNewPasswordButton: FloatingActionButton by bindView(R.id.fab_add_new_password_action)
    private val mEmptyView: View by bindView(R.id.l_empty_view)
    private val mProgressBar: ProgressBar by bindView(R.id.pb_passwords_loading)

    private var mToolbarActionMode: ActionMode? = null
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

    override fun onPause() {
        super.onPause()
        hideSelectedMode()
    }

    override fun displayPasswords(passwordModelsList: List<PasswordItemModel>) {
        mPasswordsAdapter.insertData(passwordModelsList)
    }

    override fun setLoadingState(isLoading: Boolean) {
        mProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun setEmptyPasswordsState(isEmptyViewVisible: Boolean) {
        mEmptyView.visibility = if (isEmptyViewVisible) View.VISIBLE else View.GONE
    }

    override fun openPasswordToastMode(passwordName: String, passwordContent: String) {
        val message = "$passwordName: $passwordContent"
        showShortTimeMessage(message)
    }

    override fun setPasswordVisibilityInCardMode(passwordId: Long, contentVisibilityState: Boolean) {
        mPasswordsAdapter.setPasswordContentVisibility(passwordId, contentVisibilityState)
    }

    private fun initToolbar() {
        if (activity is AppCompatActivity) {
            val appCompatActivity = activity as AppCompatActivity
            appCompatActivity.setSupportActionBar(mToolbar)
            appCompatActivity.supportActionBar?.title = "Passwords list"
        }
    }

    private fun initRecyclerView() {
        mPasswordsAdapter = PasswordsListRecyclerAdapter(
            mPasswordsListPresenter::passwordShowActionRequired,
            mPasswordsActionModePresenter::onPasswordItemSingleClick,
            mPasswordsActionModePresenter::onPasswordItemLongClick
        )

        mPasswordsRecyclerView.adapter = mPasswordsAdapter
        mPasswordsRecyclerView.layoutManager = GridLayoutManager(
            context,
            1,
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
        (mPasswordsRecyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }

    //------------------------------------- Action mode --------------------------------------------

    override fun showSelectedItemsQuantityText(text: String) {
        mToolbarActionMode?.title = text
    }

    override fun displaySelectedMode() {
        val activityOfFragment = activity
        if (mToolbarActionMode == null && activityOfFragment != null && activityOfFragment is AppCompatActivity) {
            mToolbarActionMode = activityOfFragment.startSupportActionMode(object : ActionMode.Callback {
                override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                    return when (item?.itemId) {
                        R.id.action_delete_selected_passwords -> {
                            mPasswordsActionModePresenter.onDeleteAction()
                            true
                        }
                        else -> false
                    }
                }

                override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                    mode?.menuInflater?.inflate(R.menu.passwords_list_action_mode_menu, menu)
                    return true
                }

                override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                    return false
                }

                override fun onDestroyActionMode(p0: ActionMode?) {
                    hideSelectedMode()
                }
            })
        }
    }

    override fun hideSelectedMode() {
        mPasswordsActionModePresenter.onSelectedModeFinished()
        mToolbarActionMode?.finish()
        mToolbarActionMode = null
    }

    override fun showSelectStateForItem(isSelected: Boolean, passwordId: Long) {
        mPasswordsAdapter.setSelectedStateForPasswordItemId(isSelected, passwordId)
    }

    //----------------------------------------------------------------------------------------------

    //--------------------------------------- Security ---------------------------------------------

    override fun startSecurityAuthAction() {
        activity?.let {
            SecurityActivity.startSecurityForResult(
                activity!!,
                this,
                SecurityPresenter.AUTH_SECURITY_ACTION_TYPE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            AppConstants.SECURITY_REQUEST_CODE -> handleSecurityAuthResult(resultCode)
        }
    }

    private fun handleSecurityAuthResult(resultCode: Int) {
        when (resultCode) {
            Activity.RESULT_OK -> mPasswordsListPresenter.onSecurityAuthSuccessful()
            Activity.RESULT_CANCELED -> mPasswordsListPresenter.onSecurityAuthCanceled()
        }
    }
}