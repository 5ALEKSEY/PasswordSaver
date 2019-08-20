package com.ak.passwordsaver.presentation.base

import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import com.arellomobile.mvp.MvpAppCompatActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class BasePSFragmentActivity : MvpAppCompatActivity(), IBaseAppView, HasSupportFragmentInjector {

    @Inject
    lateinit var mFragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @LayoutRes
    abstract fun getScreenLayoutResId(): Int

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        initViewBeforePresenterAttach()
        mvpDelegate.onAttach()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(getScreenLayoutResId())
        mvpDelegate.onAttach()
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return mFragmentDispatchingAndroidInjector
    }

    protected open fun initViewBeforePresenterAttach() {

    }
}