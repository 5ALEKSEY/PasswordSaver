package com.ak.passwordsaver.presentation.screens.home

import android.os.Bundle
import android.view.WindowManager
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.ak.base.extensions.removeTextBadgeByMenuId
import com.ak.base.extensions.setTextBadgeByMenuId
import com.ak.base.extensions.setVisibility
import com.ak.base.ui.BasePSFragment
import com.ak.passwordsaver.R
import com.ak.passwordsaver.di.AppComponent
import com.ak.passwordsaver.injector.ApplicationInjector
import com.ak.passwordsaver.presentation.base.BasePSFragmentActivity
import kotlinx.android.synthetic.main.activity_home.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class HomeActivity : BasePSFragmentActivity<HomePresenter>(), IHomeView {

    companion object {
        private const val MENU_CHANGE_DELAY = 200L
    }

    @InjectPresenter
    lateinit var homePresenter: HomePresenter

    @ProvidePresenter
    fun providePresenter(): HomePresenter = daggerPresenter

    private var currentMenuItemId = R.id.passwordsListFragment
    private var lastMenuChangeTime = 0L

    private val visibleBottomBarDestinations = arrayOf(
            R.id.settingsFragment,
            R.id.passwordsListFragment,
            R.id.accountsListFragment
    )

    private val homeNavController: NavController by lazy {
        Navigation.findNavController(this, R.id.bottomNavHostFragment)
    }

    private val destChangeListener =
        NavController.OnDestinationChangedListener { _, destAction, _ ->
            if (destAction !is NavGraph) {
                ApplicationInjector.onDestinationIdChanged(destAction.id)
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
        AppComponent.get().inject(this)
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

    override fun setFeatureBadgeText(featureMenuId: Int, text: String) {
        bnvBottomBar.setTextBadgeByMenuId(featureMenuId, text)
    }

    override fun removeFeatureBadgeText(featureMenuId: Int) {
        bnvBottomBar.removeTextBadgeByMenuId(featureMenuId)
    }

    private fun initBottomNavigationView(navController: NavController) {
        bnvBottomBar.setOnNavigationItemSelectedListener { menuItem ->
            if (currentMenuItemId == menuItem.itemId) {
                // skip already selected menu item
                return@setOnNavigationItemSelectedListener false
            }

            val currentTime = System.currentTimeMillis()
            val lastMenuItemChangeDuration = currentTime - lastMenuChangeTime
            if (lastMenuChangeTime != 0L && lastMenuItemChangeDuration < MENU_CHANGE_DELAY) {
                // skip monkey fast menu tabs changes
                return@setOnNavigationItemSelectedListener false
            }

            currentMenuItemId = menuItem.itemId
            lastMenuChangeTime = currentTime
            homePresenter.onNavMenuDestinationChanged(currentMenuItemId)
            NavigationUI.onNavDestinationSelected(menuItem, navController)
        }
        homePresenter.checkFeaturesBadgeUpdate()
    }

    private fun setSecureRecentAppsScreenState(isSecure: Boolean) {
        if (isSecure) {
            window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }
}
