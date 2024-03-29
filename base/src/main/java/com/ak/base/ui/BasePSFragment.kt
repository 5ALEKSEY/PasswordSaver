package com.ak.base.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ak.app_theme.theme.uicomponents.BaseThemeFragment
import com.ak.base.extensions.showToastMessage
import com.ak.base.extensions.vibrate
import com.ak.base.navigation.NavDeepLinkDestination
import com.ak.base.navigation.NavDeepLinkManager
import com.ak.base.ui.toolbar.IToolbarController
import com.ak.base.utils.LifecyclePostponedEventsManager
import com.ak.base.viewmodel.BasePSViewModel

abstract class BasePSFragment<VM : BasePSViewModel> : BaseThemeFragment() {

    protected lateinit var postponedEventManager: LifecyclePostponedEventsManager
    protected lateinit var navController: NavController
    protected lateinit var navDeepLinkManager: NavDeepLinkManager
    protected lateinit var fragmentView: View

    protected lateinit var viewModel: VM

    @LayoutRes
    abstract fun getFragmentLayoutResId(): Int

    abstract fun injectFragment(appContext: Context)

    abstract fun createViewModel(): VM

    open fun isBackPressEnabled() = true

    open fun onContextMenuClosed(menu: Menu?) {

    }

    override fun onAttach(context: Context) {
        injectFragment(context.applicationContext)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModel()
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            onBackPressed()
        }
        callback.isEnabled = isBackPressEnabled()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragmentView = inflater.inflate(
            getFragmentLayoutResId(),
            container,
            false,
        )

        postponedEventManager = LifecyclePostponedEventsManager(viewLifecycleOwner.lifecycle)

        initView(fragmentView)
        subscriberToViewModel(viewModel)
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (context?.applicationContext as? NavDeepLinkManager.Provider)?.let {
            navDeepLinkManager = it.provideNavDeepLinkManager()
        }
        navController = Navigation.findNavController(view)
    }

    @CallSuper
    protected open fun initView(fragmentView: View) {
        findViews(fragmentView)
    }

    @CallSuper
    protected open fun findViews(fragmentView: View) {

    }

    @CallSuper
    protected open fun subscriberToViewModel(viewModel: VM) {
        viewModel.subscribeToShortTimeMessageLiveData().observe(viewLifecycleOwner) {
            showToastMessage(it)
        }
        viewModel.subscribeToVibrateLiveData().observe(viewLifecycleOwner) {
            vibrate(it)
        }
    }

    protected open fun onBackPressed() {
        navController.popBackStack()
    }

    protected fun applyForToolbarController(fromZero: Boolean = true, block: IToolbarController.() -> Unit) {
        (activity as? IToolbarController)?.apply {
            if (fromZero) {
                stopToolbarTitleLoading()
                clearBackAction()
            }

            block(this)
        }
    }

    protected fun navigate(destination: NavDeepLinkDestination, shouldAnimate: Boolean = true) {
        navDeepLinkManager.navigate(
            navController = navController,
            destination = destination,
            shouldAnimate = shouldAnimate,
        )
    }
}