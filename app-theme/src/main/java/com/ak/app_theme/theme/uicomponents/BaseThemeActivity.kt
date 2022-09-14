package com.ak.app_theme.theme.uicomponents

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import androidx.annotation.AttrRes
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import com.ak.app_theme.R
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.applier.CustomThemeApplier
import com.ak.app_theme.theme.CustomThemeInterceptor
import com.ak.app_theme.theme.CustomThemeManager
import com.ak.app_theme.theme.CustomThemedView
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

abstract class BaseThemeActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "BaseThemeActivity"
    }

    private val themedViews = SparseArray<CustomThemedView>()
    private val supportThemeViews = mutableListOf<CustomTheme.Support>()
    private var themeChangeDisposable: Disposable? = null
    private var theme: CustomTheme? = null
    private var themeInflaterSubscription: Disposable? = null

    override fun attachBaseContext(newBase: Context?) {
        if (newBase == null) {
            super.attachBaseContext(newBase)
        } else {
            super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
        }
    }

    private fun attachThemeViewInflater() {
        themeInflaterSubscription = CustomThemeInterceptor.instance
            .getInflatedViewObservable()
            .filter {
                view -> view.context != null && isEnabledThemeChangeListener()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { themedView ->
                addThemedView(themedView, force = false)
                themedView.resetView()
            }
            .doOnSubscribe { enableThemeChangeListener(true) }
            .subscribe()
    }

    private fun isEnabledThemeChangeListener() = !(themeChangeDisposable?.isDisposed ?: true)

    private fun enableThemeChangeListener(enabled: Boolean) {
        if (enabled) {
            if (!isEnabledThemeChangeListener()) {
                themeChangeDisposable = CustomThemeManager.getInstance()
                    .getChangeThemeListener()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext { onThemeChanged(it) }
                    .subscribe()
            }
        } else {
            themeChangeDisposable?.dispose()
        }
    }

    private fun onThemeChanged(theme: CustomTheme) {
        this.theme = theme
        setTheme(theme.themeStyle)

        val count = themedViews.size()
        for (i in 0 until count) {
            try {
                CustomThemeApplier.applyTheme(
                    this,
                    themedViews.valueAt(i),
                    theme
                )
            } catch (e: Exception) {
                Log.e(TAG, "onThemeChanged - applyTheme", e)
            }
        }

        applyTheme(theme)
    }

    protected fun addThemedView(view: CustomTheme.Support) {
        if (!supportThemeViews.contains(view)) {
            supportThemeViews.add(view)
        }
    }

    protected fun addThemedView(viewId: Int) {
        addThemedView(
            CustomThemedView.Builder(this)
                .setViewId(viewId)
                .build()
        )
    }

    protected fun addThemedViewWithBackground(viewId: Int, @AttrRes backgroundAttr: Int) {
        addThemedView(
            CustomThemedView.Builder(this)
                .setViewId(viewId)
                .setBackground(backgroundAttr)
                .build()
        )
    }

    protected fun addThemedViewWithBackgroundTint(viewId: Int, @AttrRes backgroundAttr: Int) {
        addThemedView(
            CustomThemedView.Builder(this)
                .setViewId(viewId)
                .setBackgroundTint(backgroundAttr)
                .build()
        )
    }

    protected fun addThemedViewWithTextColor(viewId: Int, @AttrRes textColorAttr: Int) {
        addThemedView(
            CustomThemedView.Builder(this)
                .setViewId(viewId)
                .setTextColor(textColorAttr)
                .build()
        )
    }

    protected fun addThemedViewWithDivider(viewId: Int, @AttrRes dividerAttr: Int) {
        addThemedView(
            CustomThemedView.Builder(this)
                .setViewId(viewId)
                .setDividerResource(dividerAttr)
                .build()
        )
    }

    @JvmOverloads
    protected fun addThemedView(themedView: CustomThemedView, force: Boolean = true) {
        val viewId = themedView.viewId

        if (isExistsThemedView(viewId) && !force) {
            return
        }

        try {
            theme?.let {
                CustomThemeApplier.applyTheme(
                    this,
                    themedView,
                    it
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "addThemedView - applyTheme", e)
        }

        if (viewId != -1) {
            themedViews.put(viewId, themedView)
        }
    }

    private fun isExistsThemedView(viewId: Int) = themedViews.get(viewId, null) != null

    private fun getCustomTheme(): CustomTheme {
        val themeMgr = CustomThemeManager.getInstance()
        themeMgr.refreshTheme(this)
        return themeMgr.getTheme()
    }

    private fun analyzeTheme() {
        getCustomTheme().let {
            if (theme?.id != it.id) {
                theme = it
                onThemeChanged(it)
            }
        }
    }

    @CallSuper
    protected open fun applyTheme(theme: CustomTheme) {
        CustomThemeApplier.applyForWindow(
            theme,
            window,
            getStatusBarColorResource(),
            getNavigationBarColorResource(),
        )
        CustomThemeApplier.applyWindowBackground(
            theme,
            window,
            getWindowBackground()
        )

        for (supportThemeView in supportThemeViews) {
            supportThemeView.applyTheme(theme)
        }
    }

    @AttrRes
    protected open fun getWindowBackground(): Int {
        return R.attr.themedPrimaryBackgroundColor
    }

    @AttrRes
    protected open fun getStatusBarColorResource(): Int {
        return R.attr.themedPrimaryColor
    }

    @AttrRes
    protected open fun getNavigationBarColorResource(): Int {
        return R.attr.themedPrimaryBackgroundColor
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getCustomTheme().let {
            theme = it
            setTheme(it.themeStyle)
        }
        super.onCreate(savedInstanceState)
        attachThemeViewInflater()
    }

    override fun onStart() {
        getCustomTheme().let {
            if (theme?.id == it.id) {
                applyTheme(it)
            }
        }
        super.onStart()
    }

    public override fun onResume() {
        super.onResume()

        analyzeTheme()
        enableThemeChangeListener(true)
    }

    override fun onPause() {
        enableThemeChangeListener(false)
        super.onPause()
    }

    override fun onDestroy() {
        themeInflaterSubscription?.dispose()
        themeChangeDisposable?.dispose()
        supportThemeViews.clear()

        for (index in 0 until themedViews.size()) {
            val key = themedViews.keyAt(index)
            themedViews[key].destroy()
        }
        themedViews.clear()

        super.onDestroy()
    }
}