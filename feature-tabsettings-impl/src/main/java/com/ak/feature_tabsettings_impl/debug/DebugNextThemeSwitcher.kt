package com.ak.feature_tabsettings_impl.debug

import android.content.Context
import com.ak.app_theme.theme.CustomThemeManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

object DebugNextThemeSwitcher {

    const val NEXT_THEME_SWITCH_PERIOD_IN_SECONDS = 8L

    private var themeSwitchDisposable: Disposable? = null

    fun isThemeSwitchingEnabled() = themeSwitchDisposable?.let { !it.isDisposed } ?: false

    fun startNextThemeSwitching(context: Context) {
        stopNextThemeSwitching()
        themeSwitchDisposable = Observable.interval(
            0,
            NEXT_THEME_SWITCH_PERIOD_IN_SECONDS,
            TimeUnit.SECONDS,
            Schedulers.computation(),
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                CustomThemeManager.getInstance().setNextTheme(context)
            }
    }

    fun stopNextThemeSwitching() {
        themeSwitchDisposable?.dispose()
        themeSwitchDisposable = null
    }
}