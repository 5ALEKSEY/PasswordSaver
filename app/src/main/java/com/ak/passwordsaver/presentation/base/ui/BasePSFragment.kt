package com.ak.passwordsaver.presentation.base.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.ak.passwordsaver.utils.extensions.showToastMessage
import com.ak.passwordsaver.utils.extensions.vibrate
import dagger.android.support.AndroidSupportInjection
import moxy.MvpAppCompatFragment

abstract class BasePSFragment : MvpAppCompatFragment(), IBaseAppView {

    @LayoutRes
    abstract fun getFragmentLayoutResId(): Int

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getFragmentLayoutResId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mvpDelegate.onAttach()
        initViewAfterPresenterAttach()
    }

    override fun showShortTimeMessage(message: String) {
        context?.showToastMessage(message)
    }

    override fun invokeVibration(vibrateDuration: Long) {
        context?.vibrate(vibrateDuration)
    }

    protected open fun initViewAfterPresenterAttach() {

    }
}