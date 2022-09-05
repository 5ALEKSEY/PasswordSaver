package com.ak.passwordsaver.presentation.base

import android.content.Intent
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import com.ak.app_theme.theme.uicomponents.BaseThemeActivity
import com.ak.base.extensions.showToastMessage
import com.ak.base.extensions.vibrate
import com.ak.base.viewmodel.BasePSViewModel
import com.ak.feature_security_api.interfaces.IAuthCheckerStarter
import com.ak.feature_security_api.interfaces.IPSAuthManager
import javax.inject.Inject

// TODO: must be in base module (but auth manager dependency should be solved before)
abstract class BasePSFragmentActivity<VM : BasePSViewModel> : BaseThemeActivity() {

    @Inject
    protected lateinit var authManager: IPSAuthManager
    @Inject
    protected lateinit var authChecker: IAuthCheckerStarter

    protected lateinit var viewModel: VM

    @LayoutRes
    abstract fun getScreenLayoutResId(): Int

    abstract fun createViewModel(): VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getScreenLayoutResId())
        viewModel = createViewModel()
        initView()
        subscriberToViewModel(viewModel)
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

    @CallSuper
    protected open fun initView() {

    }

    @CallSuper
    protected open fun subscriberToViewModel(viewModel: VM) {
        viewModel.subscribeToShortTimeMessageLiveData().observe(this) {
            showToastMessage(it)
        }
        viewModel.subscribeToVibrateLiveData().observe(this) {
            vibrate(it)
        }
    }

    protected open fun isAuthCheckNeedsForScreen() = true
}
