package com.ak.passwordsaver.presentation.screens.home

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.ak.base.extensions.removeTextBadgeByMenuId
import com.ak.base.extensions.setTextBadgeByMenuId
import com.ak.base.extensions.setVisibility
import com.ak.base.extensions.turnOffToolbarScrolling
import com.ak.base.extensions.turnOnToolbarScrolling
import com.ak.base.ui.BasePSFragment
import com.ak.base.ui.toolbar.IToolbarController
import com.ak.base.viewmodel.injectViewModel
import com.ak.passwordsaver.R
import com.ak.passwordsaver.di.AppComponent
import com.ak.passwordsaver.di.modules.MainViewModelsModule
import com.ak.passwordsaver.injector.ClearComponentsByDestinationChangeManager
import com.ak.passwordsaver.presentation.base.BasePSFragmentActivity
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import javax.inject.Inject
import javax.inject.Named

typealias ToolbarAction = () -> Unit

class HomeActivity : BasePSFragmentActivity<HomeViewModel>(), IToolbarController {

    companion object {
        private const val MENU_CHANGE_DELAY = 200L
    }

    @Inject
    @field:Named(MainViewModelsModule.MAIN_VIEW_MODELS_FACTORY_KEY)
    protected lateinit var viewModelsFactory: ViewModelProvider.Factory

    private var currentMenuItemId = R.id.passwordsListFragment
    private var lastMenuChangeTime = 0L

    private val toolbarPostponedActions = mutableListOf<ToolbarAction>()

    private val visibleBottomBarDestinations = arrayOf(
        R.id.settingsFragment,
        R.id.passwordsListFragment,
        R.id.accountsListFragment
    )

    private val homeNavController: NavController by lazy {
        Navigation.findNavController(this, R.id.bottomNavHostFragment)
    }
    private val bottomNavigationView by lazy {
        findViewById<BottomNavigationView>(R.id.bnvBottomBar)
    }
    private val toolbarAppBarLayout by lazy {
        findViewById<AppBarLayout>(R.id.ablHomeBarLayout)
    }
    private val toolbarView by lazy {
        findViewById<Toolbar>(R.id.tbHomeToolbar)
    }

    private val destChangeListener = NavController.OnDestinationChangedListener { _, destAction, _ ->
        if (destAction !is NavGraph) {
            val appContext = applicationContext
            if (appContext is ClearComponentsByDestinationChangeManager) {
                appContext.onDestinationIdChanged(destAction.id)
            }
            bottomNavigationView.setVisibility(destAction.id in visibleBottomBarDestinations)
        }
    }

    override fun getScreenLayoutResId() = R.layout.activity_home

    override fun createViewModel(): HomeViewModel {
        return injectViewModel(viewModelsFactory)
    }

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

        viewModel.finishScreenAction()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppComponent.get().inject(this)
        super.onCreate(savedInstanceState)
        initToolbar()
        setSecureRecentAppsScreenState(viewModel.getSecureApplicationState())
    }

    override fun onStart() {
        homeNavController.addOnDestinationChangedListener(destChangeListener)
        super.onStart()
    }

    override fun onStop() {
        homeNavController.removeOnDestinationChangedListener(destChangeListener)
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        toolbarPostponedActions.clear()
    }

    override fun initView() {
        super.initView()
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            if (currentMenuItemId == menuItem.itemId) {
                // skip already selected menu item
                return@setOnItemSelectedListener false
            }

            val currentTime = System.currentTimeMillis()
            val lastMenuItemChangeDuration = currentTime - lastMenuChangeTime
            if (lastMenuChangeTime != 0L && lastMenuItemChangeDuration < MENU_CHANGE_DELAY) {
                // skip monkey fast menu tabs changes
                return@setOnItemSelectedListener false
            }

            currentMenuItemId = menuItem.itemId
            lastMenuChangeTime = currentTime
            viewModel.onNavMenuDestinationChanged(currentMenuItemId)
            NavigationUI.onNavDestinationSelected(menuItem, homeNavController)
        }
        viewModel.checkFeaturesBadgeUpdate()
    }

    override fun subscriberToViewModel(viewModel: HomeViewModel) {
        super.subscriberToViewModel(viewModel)
        viewModel.subscribeToFinishScreenLiveData().observe(this) {
            finish()
        }
        viewModel.subscribeToFeatureBadgeTextLiveData().observe(this) { badgeData ->
            val (badgeId, badgeTextResId) = badgeData
            if (badgeTextResId == null || badgeTextResId == -1) {
                bottomNavigationView.removeTextBadgeByMenuId(badgeId)
            } else {
                bottomNavigationView.setTextBadgeByMenuId(badgeId, getString(badgeTextResId))
            }
        }
    }

    override fun setToolbarTitle(titleResIs: Int) {
        setToolbarTitle(getString(titleResIs))
    }

    override fun setToolbarTitle(title: String) {
        forToolbarOrPostpone { supportActionBar?.title = title }
    }

    override fun switchToolbarScrollingState(isScrollingEnabled: Boolean) {
        forToolbarOrPostpone {
            if (isScrollingEnabled) {
                toolbarView.turnOnToolbarScrolling(toolbarAppBarLayout)
            } else {
                toolbarView.turnOffToolbarScrolling(toolbarAppBarLayout)
            }
        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbarView)

        toolbarPostponedActions.forEach { it.invoke() }
        toolbarPostponedActions.clear()
    }

    private fun forToolbarOrPostpone(block: ToolbarAction) {
        if (supportActionBar != null) {
            block()
        } else {
            toolbarPostponedActions.add(block)
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
