package com.ak.passwordsaver

import android.app.Activity
import android.app.Application
import com.ak.passwordsaver.di.AppComponent
import com.ak.passwordsaver.di.DaggerAppComponent
import com.ak.passwordsaver.di.modules.AppModule
import com.facebook.stetho.Stetho
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class PSApplication : Application(), HasActivityInjector {

    companion object {
        lateinit var appInstance: PSApplication
    }

    private lateinit var mAppComponent: AppComponent

    @Inject
    lateinit var mActivityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    fun getApplicationComponent() = mAppComponent

    override fun onCreate() {
        super.onCreate()
        appInstance = this

        mAppComponent = buildApplicationDaggerComponent()
        mAppComponent.inject(this)

        // Init Stetho
        Stetho.initializeWithDefaults(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = mActivityDispatchingAndroidInjector

    private fun buildApplicationDaggerComponent() =
        DaggerAppComponent.builder().appModule(AppModule(appInstance)).build()
}
