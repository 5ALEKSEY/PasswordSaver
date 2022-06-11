package com.ak.passwordsaver

import android.app.Application
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.ak.app_theme.theme.CustomThemeInterceptor
import com.ak.app_theme.theme.CustomThemeManager
import com.ak.feature_security_api.interfaces.IPSAuthManager
import com.ak.feature_tabpasswords_impl.screens.navigation.cross.PasswordsTabCrossModuleNavigatorProvider
import com.ak.passwordsaver.appnavigator.IAppNavigator
import com.ak.passwordsaver.injector.ApplicationComponentsManager
import com.ak.passwordsaver.injector.ApplicationComponentsManagerImpl
import io.github.inflationx.viewpump.ViewPump
import javax.inject.Inject

open class PSApplication : Application(), LifecycleObserver,
    PasswordsTabCrossModuleNavigatorProvider,
    ApplicationComponentsManager by ApplicationComponentsManagerImpl() {

    companion object {
        lateinit var appContext: Context
    }

    @Inject
    internal lateinit var mAuthManager: IPSAuthManager
    @Inject
    internal lateinit var appNavigator: IAppNavigator

    private val lifecycleEventObserver = LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_STOP -> {
                if (mAuthManager.isLockAppSetAllowable()) {
                    mAuthManager.setAppLockState(true)
                }
            }
            Lifecycle.Event.ON_RESUME -> {
                if (mAuthManager.isLockAppSetAllowable()) {
                    mAuthManager.setAppLockState(false)
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        initCustomTheme()
        initializeAppComponent().apply {
            inject(this@PSApplication)
        }
        ProcessLifecycleOwner.get().lifecycle.addObserver(lifecycleEventObserver)
    }

    private fun initCustomTheme() {
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(CustomThemeInterceptor.instance)
                .build()
        )
        CustomThemeManager.getInstance().init(this)
    }

    override fun provideCrossNavigatorForPasswordsModule() = appNavigator
}
