package com.ak.passwordsaver

import android.app.Activity
import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ProcessLifecycleOwner
import com.ak.passwordsaver.di.AppComponent
import com.ak.passwordsaver.di.DaggerAppComponent
import com.ak.passwordsaver.di.modules.AppModule
import com.ak.passwordsaver.presentation.base.managers.auth.IPSAuthManager
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

open class PSApplication : Application(), HasActivityInjector, LifecycleObserver {

    companion object {
        lateinit var appInstance: PSApplication
    }

    private lateinit var mAppComponent: AppComponent

    @Inject
    lateinit var mActivityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    @Inject
    lateinit var mAuthManager: IPSAuthManager

    fun getApplicationComponent() = mAppComponent

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        appInstance = this

        mAppComponent = buildApplicationDaggerComponent()
        mAppComponent.inject(this)
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
}
