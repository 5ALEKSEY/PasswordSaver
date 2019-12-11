package com.ak.passwordsaver.presentation.screens.home

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.MenuItem
import android.view.WindowManager
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.base.ui.BasePSFragmentActivity
import com.ak.passwordsaver.presentation.screens.passwords.PasswordsListFragment
import com.ak.passwordsaver.presentation.screens.settings.SettingsFragment
import com.ak.passwordsaver.utils.bindView
import com.arellomobile.mvp.presenter.InjectPresenter

class HomeActivity : BasePSFragmentActivity(), BottomNavigationView.OnNavigationItemSelectedListener, IHomeView {

    @InjectPresenter
    lateinit var mHomePresenter: HomePresenter

    private val mBottomMenu: BottomNavigationView by bindView(R.id.bnv_bottom_bar)

    override fun getScreenLayoutResId() = R.layout.activity_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showFragment(PasswordsListFragment.getInstance())
        mBottomMenu.setOnNavigationItemSelectedListener(this)
    }

    override fun initViewBeforePresenterAttach() {
        super.initViewBeforePresenterAttach()
        setSecureRecentAppsScreenState(mHomePresenter.getSecureApplicationState())
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_passwords_list -> {
                showFragment(PasswordsListFragment.getInstance())
                true
            }
            R.id.action_settings -> {
                showFragment(SettingsFragment.getInstance())
                true
            }
            else -> false
        }
    }

    override fun onBackPressed() {
        mHomePresenter.finishScreenAction()
    }

    override fun finishScreen() {
        finish()
    }

    private fun setSecureRecentAppsScreenState(isSecure: Boolean) {
        if (isSecure) {
            window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }

    private fun showFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        fragmentTransaction.replace(getFragmentContainerId(), fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    private fun getFragmentContainerId() = R.id.fl_fragments_container
}
