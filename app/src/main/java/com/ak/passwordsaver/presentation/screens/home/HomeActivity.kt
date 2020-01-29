package com.ak.passwordsaver.presentation.screens.home

import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.base.ui.BasePSFragmentActivity
import com.ak.passwordsaver.presentation.screens.passwords.PasswordsListFragment
import com.ak.passwordsaver.presentation.screens.settings.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.Lazy
import kotlinx.android.synthetic.main.activity_home.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class HomeActivity : BasePSFragmentActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener, IHomeView {

    @Inject
    lateinit var daggerPresenter: Lazy<HomePresenter>

    @InjectPresenter
    lateinit var mHomePresenter: HomePresenter

    @ProvidePresenter
    fun providePresenter(): HomePresenter = daggerPresenter.get()

    override fun getScreenLayoutResId() = R.layout.activity_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showFragment(PasswordsListFragment.getInstance())
        bnvBottomBar.setOnNavigationItemSelectedListener(this)
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

    private fun getFragmentContainerId() = R.id.cFragmentsContainer
}
