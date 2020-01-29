package com.ak.passwordsaver.presentation.base.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.ak.passwordsaver.presentation.base.BasePSPresenter
import com.ak.passwordsaver.presentation.base.constants.AppConstants
import com.ak.passwordsaver.presentation.base.managers.auth.IPSAuthManager
import com.ak.passwordsaver.presentation.screens.auth.SecurityActivity
import com.ak.passwordsaver.presentation.screens.auth.SecurityPresenter
import com.ak.passwordsaver.utils.extensions.showToastMessage
import com.ak.passwordsaver.utils.extensions.vibrate
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import moxy.MvpAppCompatActivity
import javax.inject.Inject

abstract class BasePSFragmentActivity<Presenter : BasePSPresenter<*>> : MvpAppCompatActivity(),
    IBaseAppView,
    HasSupportFragmentInjector {

    @Inject
    lateinit var mFragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var mPSAuthManager: IPSAuthManager

    @Inject
    lateinit var daggerPresenter: Presenter

    @LayoutRes
    abstract fun getScreenLayoutResId(): Int

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        mvpDelegate.onAttach()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(getScreenLayoutResId())
        initViewBeforePresenterAttach()
        mvpDelegate.onAttach()
    }

    override fun onResume() {
        super.onResume()
        if (mPSAuthManager.isAppLocked() && isAuthCheckNeedsForScreen()) {
            SecurityActivity.startSecurityForResult(
                this,
                SecurityPresenter.AUTH_SECURITY_ACTION_TYPE,
                AppConstants.SECURITY_AUTH_ACTION_REQUEST_CODE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppConstants.SECURITY_AUTH_ACTION_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> mPSAuthManager.setAppLockState(false)
                else -> {
                    finishAffinity()
                }
            }
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return mFragmentDispatchingAndroidInjector
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