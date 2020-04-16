package com.ak.passwordsaver.presentation.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import com.ak.base.constants.AppConstants
import com.ak.base.extensions.showToastMessage
import com.ak.base.extensions.vibrate
import com.ak.base.presenter.BasePSPresenter
import com.ak.base.ui.IBaseAppView
import com.ak.feature_security_api.interfaces.IPSAuthManager
import moxy.MvpAppCompatActivity
import javax.inject.Inject

abstract class BasePSFragmentActivity<Presenter : BasePSPresenter<*>> : MvpAppCompatActivity(),
    IBaseAppView {

    @Inject
    lateinit var authManager: IPSAuthManager

    @Inject
    lateinit var daggerPresenter: Presenter

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
            com.ak.feature_security_impl.auth.SecurityActivity.startSecurityForResult(
                this,
                IPSAuthManager.AUTH_SECURITY_ACTION_TYPE,
                AppConstants.SECURITY_AUTH_ACTION_REQUEST_CODE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppConstants.SECURITY_AUTH_ACTION_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> authManager.setAppLockState(false)
                else -> {
                    finishAffinity()
                }
            }
        }
    }

    override fun showShortTimeMessage(message: String) {
        showToastMessage(message)
    }

    override fun invokeVibration(vibrateDuration: Long) {
        vibrate(vibrateDuration)
    }

    protected open fun initViewBeforePresenterAttach() {

    }

    protected open fun isAuthCheckNeedsForScreen() = true
}