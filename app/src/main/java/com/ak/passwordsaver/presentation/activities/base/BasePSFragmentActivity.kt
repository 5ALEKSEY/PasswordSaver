package com.ak.passwordsaver.presentation.activities.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import com.ak.passwordsaver.presentation.view.base.IBaseAppView
import com.arellomobile.mvp.MvpAppCompatActivity

abstract class BasePSFragmentActivity : MvpAppCompatActivity(), IBaseAppView {

    @LayoutRes
    abstract fun getScreenLayoutResId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getScreenLayoutResId())
    }
}