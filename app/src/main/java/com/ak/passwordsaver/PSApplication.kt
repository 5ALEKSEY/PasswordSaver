package com.ak.passwordsaver

import android.app.Application
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.ak.feature_security_api.interfaces.IPSAuthManager
import com.ak.feature_tabpasswords_impl.screens.navigation.cross.PasswordsTabCrossModuleNavigatorProvider
import com.ak.passwordsaver.appnavigator.IAppNavigator
import com.ak.passwordsaver.injector.ApplicationInjector
import javax.inject.Inject

open class PSApplication : Application(), LifecycleObserver,
    PasswordsTabCrossModuleNavigatorProvider {

    companion object {
        lateinit var appContext: Context
    }

    @Inject
    internal lateinit var mAuthManager: IPSAuthManager
    @Inject
    internal lateinit var appNavigator: IAppNavigator

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        ApplicationInjector.initAppComponent().apply {
            inject(this@PSApplication)
        }

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppProcessBackgrounded() {
        if (mAuthManager.isLockAppSetAllowable()) {
            mAuthManager.setAppLockState(true)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onAppProcessForegrounded() {
        if (mAuthManager.isLockAppSetAllowable()) {
            mAuthManager.setAppLockState(false)
        }
    }

    override fun provideCrossNavigatorForPasswordsModule() = appNavigator
}
