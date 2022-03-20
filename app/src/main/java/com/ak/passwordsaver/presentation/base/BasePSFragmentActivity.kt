package com.ak.passwordsaver.presentation.base

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.ak.base.extensions.showToastMessage
import com.ak.base.extensions.vibrate
import com.ak.base.presenter.BasePSPresenter
import com.ak.base.ui.BasePSFragment
import com.ak.base.ui.IBaseAppView
import com.ak.feature_security_api.interfaces.IAuthCheckerStarter
import com.ak.feature_security_api.interfaces.IPSAuthManager
import javax.inject.Inject
import moxy.MvpAppCompatActivity

abstract class BasePSFragmentActivity<Presenter : BasePSPresenter<*>> : MvpAppCompatActivity(),
    IBaseAppView {

    @Inject
    protected lateinit var authManager: IPSAuthManager
    @Inject
    protected lateinit var authChecker: IAuthCheckerStarter

    @Inject
    protected lateinit var daggerPresenter: Presenter

    @LayoutRes
    abstract fun getScreenLayoutResId(): Int

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        mvpDelegate.onAttach()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getScreenLayoutResId())
        initViewBeforePresenterAttach()
        mvpDelegate.onAttach()
    }

    override fun onResume() {
        super.onResume()
        if (authManager.isAppLocked() && isAuthCheckNeedsForScreen()) {
            authChecker.startAuthCheck(
                    this,
                    IAuthCheckerStarter.AUTH_SECURITY_ACTION_TYPE,
                    object : IAuthCheckerStarter.CheckAuthCallback {
                        override fun onAuthFailed() {
                            finishAffinity()
                        }
                    }
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        authChecker.handleActivityResult(requestCode, resultCode)
    }

    override fun showShortTimeMessage(message: String) {
        showToastMessage(message)
    }

    override fun invokeVibration(vibrateDuration: Long) {
        vibrate(vibrateDuration)
    }

    override fun onContextMenuClosed(menu: Menu?) {
        super.onContextMenuClosed(menu)
        supportFragmentManager.fragments.forEach {
            notifyFragmentAboutContextMenuClosed(it, menu)
        }
    }

    protected open fun initViewBeforePresenterAttach() {

    }

    protected open fun isAuthCheckNeedsForScreen() = true

    private fun notifyFragmentAboutContextMenuClosed(fragment: Fragment, menu: Menu?) {
        if (fragment is BasePSFragment<*> && fragment.isVisible) {
            fragment.onContextMenuClosed(menu)
        }
        fragment.childFragmentManager.fragments.forEach { notifyFragmentAboutContextMenuClosed(it, menu) }
    }
}
