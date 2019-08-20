package com.ak.passwordsaver

import android.app.Activity
import android.app.Application
import com.ak.passwordsaver.di.AppComponent
import com.ak.passwordsaver.di.AppModule
import com.ak.passwordsaver.di.DaggerAppComponent
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

    override fun onCreate() {
        super.onCreate()
        appInstance = this

        mAppComponent = buildApplicationDaggerComponent()
    }

    override fun activityInjector(): AndroidInjector<Activity> = mActivityDispatchingAndroidInjector

    private fun buildApplicationDaggerComponent() =
        DaggerAppComponent.builder().appModule(AppModule(appInstance)).build()
}
