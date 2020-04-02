package com.ak.passwordsaver

import android.app.Activity
import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.ak.passwordsaver.appnavigator.IAppNavigator
import com.ak.passwordsaver.auth.IPSAuthManager
import com.ak.passwordsaver.di.AppComponent
import com.ak.passwordsaver.di.DaggerAppComponent
import com.ak.passwordsaver.di.modules.AppModule
import com.ak.tabpasswords.di.PasswordsComponent
import com.ak.tabpasswords.di.PasswordsComponentProvider
import com.ak.tabpasswords.navigation.cross.PasswordsTabCrossModuleNavigatorProvider
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

open class PSApplication : Application(), HasActivityInjector, LifecycleObserver,
    PasswordsComponentProvider, PasswordsTabCrossModuleNavigatorProvider {

    companion object {
        lateinit var appInstance: PSApplication
    }

    private var applicationDaggerComponent: AppComponent? = null

    @Inject
    internal lateinit var mActivityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    @Inject
    internal lateinit var mAuthManager: IPSAuthManager
    @Inject
    internal lateinit var appNavigator: IAppNavigator

    fun getApplicationComponent(): AppComponent {
        return applicationDaggerComponent
            ?: throw IllegalStateException("Error!!! null dagger application component")
    }

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        appInstance = this

        applicationDaggerComponent = buildApplicationDaggerComponent()?.apply {
            inject(this@PSApplication)
        }
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

    override fun activityInjector(): AndroidInjector<Activity> = mActivityDispatchingAndroidInjector

    private fun buildApplicationDaggerComponent() =
        DaggerAppComponent.builder().appModule(AppModule(appInstance)).build()

    override fun providePasswordsComponent(): PasswordsComponent {
        return applicationDaggerComponent?.initPasswordsComponent()
            ?: throw IllegalStateException("Error!!! null dagger application component")
    }

    override fun provideCrossNavigatorForPasswordsModule() = appNavigator
}
