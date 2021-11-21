package com.ak.base.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.annotation.LayoutRes
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ak.base.extensions.showToastMessage
import com.ak.base.extensions.vibrate
import com.ak.base.presenter.BasePSPresenter
import moxy.MvpAppCompatFragment
import javax.inject.Inject

abstract class BasePSFragment<Presenter : BasePSPresenter<*>> : MvpAppCompatFragment(),
    IBaseAppView {

    @Inject
    lateinit var daggerPresenter: Presenter

    @LayoutRes
    abstract fun getFragmentLayoutResId(): Int

    abstract fun injectFragment()

    protected lateinit var navController: NavController
    protected lateinit var fragmentView: View

    open fun isBackPressEnabled() = true

    override fun onAttach(context: Context) {
        injectFragment()
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            onBackPressed()
        }
        callback.isEnabled = isBackPressEnabled()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentView = inflater.inflate(
                getFragmentLayoutResId(),
                container,
                false
        )
        initViewBeforePresenterAttach(fragmentView)
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        mvpDelegate.onAttach()
    }

    override fun showShortTimeMessage(message: String) {
        showToastMessage(message)
    }

    override fun invokeVibration(vibrateDuration: Long) {
        vibrate(vibrateDuration)
    }

    protected open fun initViewBeforePresenterAttach(fragmentView: View) {

    }

    protected open fun onBackPressed() {
        navController.popBackStack()
    }
}