package com.ak.app_theme.theme.uicomponents

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.CustomThemeManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

abstract class BaseThemeFragment : Fragment() {

    private var themeId = -1
    private val supportThemeViews = ArrayList<CustomTheme.Support>()

    private var themeChangeDisposable: Disposable? = null

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            analyzeTheme()
        }
    }

    protected fun addThemedView(view: CustomTheme.Support?) {
        if (view != null && !supportThemeViews.contains(view)) {
            supportThemeViews.add(view)
        }
    }

    private fun analyzeTheme() {
        setTheme(CustomThemeManager.getCurrentAppliedTheme())
    }

    fun setTheme(theme: CustomTheme) {
        if (themeId == -1 || themeId != theme.id) {
            themeId = theme.id
            applyTheme(theme)
        }
    }

    @CallSuper
    protected open fun applyTheme(theme: CustomTheme) {
        for (supportThemeView in supportThemeViews) {
            supportThemeView.applyTheme(theme)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        analyzeTheme()
    }

    override fun onDestroyView() {
        supportThemeViews.clear()
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        if (isVisible) {
            analyzeTheme()
        }

        enableThemeChangeListener(true)
    }

    override fun onPause() {
        super.onPause()

        enableThemeChangeListener(false)
    }

    private fun isEnabledThemeChangeListener() = !(themeChangeDisposable?.isDisposed ?: true)

    private fun enableThemeChangeListener(enabled: Boolean) {
        if (enabled) {
            if (!isEnabledThemeChangeListener()) {
                themeChangeDisposable = CustomThemeManager.getInstance()
                    .getChangeThemeListener()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext { setTheme(it) }
                    .subscribe()
            }
        } else {
            themeChangeDisposable?.dispose()
        }
    }
}