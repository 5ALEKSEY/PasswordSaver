package com.ak.passwordsaver.presentation.screens.home

import android.os.Bundle
import android.view.WindowManager
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.base.ui.BasePSFragment
import com.ak.passwordsaver.presentation.base.ui.BasePSFragmentActivity
import com.ak.passwordsaver.utils.extensions.setVisibility
import kotlinx.android.synthetic.main.activity_home.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class HomeActivity : BasePSFragmentActivity<HomePresenter>(), IHomeView {

    @InjectPresenter
    lateinit var homePresenter: HomePresenter

    @ProvidePresenter
    fun providePresenter(): HomePresenter = daggerPresenter

    private var currentMenuItemId = R.id.passwordsListFragment

    private val visibleBottomBarDestinations = arrayOf(
        R.id.settingsFragment,
        R.id.passwordsListFragment
    )

    private val homeNavController: NavController by lazy {
        Navigation.findNavController(this, R.id.bottomNavHostFragment)
    }

    private val destChangeListener =
        NavController.OnDestinationChangedListener { _, destAction, _ ->
            if (destAction !is NavGraph) {
                bnvBottomBar.setVisibility(destAction.id in visibleBottomBarDestinations)
            }
        }

    override fun getScreenLayoutResId() = R.layout.activity_home

    override fun onBackPressed() {
        val hostFragment = supportFragmentManager.findFragmentById(R.id.bottomNavHostFragment)
                as NavHostFragment

        // get current visible fragment in bav host container
        val inflatedFragments = hostFragment.childFragmentManager.fragments
        val currentFragment = if (inflatedFragments.isNotEmpty()) {
            inflatedFragments[0] // first (top) element in fragments stack
        } else {
            null
        }
        if (currentFragment != null
            && currentFragment is BasePSFragment<*>
            && currentFragment.isBackPressEnabled()
        ) {
            super.onBackPressed()
            return
        }

        homePresenter.finishScreenAction()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSecureRecentAppsScreenState(homePresenter.getSecureApplicationState())
        initBottomNavigationView(homeNavController)
    }

    override fun onStart() {
        homeNavController.addOnDestinationChangedListener(destChangeListener)
        super.onStart()
    }

    override fun onStop() {
        homeNavController.removeOnDestinationChangedListener(destChangeListener)
        super.onStop()
    }

    override fun finishScreen() {
        finish()
    }

    private fun initBottomNavigationView(navController: NavController) {
        bnvBottomBar.setOnNavigationItemSelectedListener { menuItem ->
            if (currentMenuItemId == menuItem.itemId) {
                // skip already selected menu item
                return@setOnNavigationItemSelectedListener false
            }
            currentMenuItemId = menuItem.itemId
            NavigationUI.onNavDestinationSelected(menuItem, navController)
        }
    }

    private fun setSecureRecentAppsScreenState(isSecure: Boolean) {
        if (isSecure) {
            window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }
}
