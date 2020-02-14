package com.ak.passwordsaver.presentation.screens.home

import android.os.Bundle
import android.view.WindowManager
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.base.ui.BasePSFragmentActivity
import kotlinx.android.synthetic.main.activity_home.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class HomeActivity : BasePSFragmentActivity<HomePresenter>(), IHomeView {

    @InjectPresenter
    lateinit var homePresenter: HomePresenter

    @ProvidePresenter
    fun providePresenter(): HomePresenter = daggerPresenter

    override fun getScreenLayoutResId() = R.layout.activity_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSecureRecentAppsScreenState(homePresenter.getSecureApplicationState())
        initBottomNavigationView()
    }

    override fun onBackPressed() {
        homePresenter.finishScreenAction()
    }

    override fun finishScreen() {
        finish()
    }

    private fun initBottomNavigationView() {
        NavigationUI.setupWithNavController(
            bnvBottomBar,
            Navigation.findNavController(this, R.id.bottomNavHostFragment)
        )
    }

    private fun setSecureRecentAppsScreenState(isSecure: Boolean) {
        if (isSecure) {
            window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }
}
