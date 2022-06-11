package com.ak.app_theme.theme

import com.ak.app_theme.R
import io.github.inflationx.viewpump.InflateResult
import io.github.inflationx.viewpump.Interceptor
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class CustomThemeInterceptor private constructor() : Interceptor {

    companion object {
        private const val ANDROID_SCHEME = "http://schemas.android.com/apk/res/android"
        private const val APP_SCHEME = "http://schemas.android.com/apk/res-auto"
        private const val THEME_PREFIX = "theme"

        val instance by lazy { CustomThemeInterceptor() }
    }

    private val inflatedViewPublishSubject = PublishSubject.create<CustomThemedView>()

    fun getInflatedViewObservable(): Observable<CustomThemedView> = inflatedViewPublishSubject

    override fun intercept(chain: Interceptor.Chain): InflateResult {
        val result = chain.proceed(chain.request())

        if (result.view != null && result.attrs != null) {
            val foundAttributes = HashMap<String, Any>()

            //printAttributes(result)

            val tagResValue = result.attrs!!.getAttributeResourceValue(ANDROID_SCHEME, "tag", 0)
            if (tagResValue == R.string.skipThemeInterceptor) { // skip
                return result
            }

            for (name in CustomThemedView.androidAttributes) {
                val resourceId = getThemedResourceId(result, ANDROID_SCHEME, name)
                if (resourceId > 0) {
                    foundAttributes[name] = resourceId
                }
            }

            for (name in CustomThemedView.appAttributes) {
                val resourceId = getThemedResourceId(result, APP_SCHEME, name)
                if (resourceId > 0) {
                    foundAttributes[name] = resourceId
                }
            }

            for (name in CustomThemedView.alphaAttributes) {
                val value = result.attrs?.getAttributeFloatValue(APP_SCHEME, name, -1.0f) ?: continue
                if (value in 0.0..1.0) {
                    foundAttributes[name] = value
                }
            }

            result.view?.let {
                if (foundAttributes.isNotEmpty() || it is CustomTheme.Support) {
                    val themedView = CustomThemedView.Builder(result.context)
                        .setViewId(it.id)
                        .setViewClassName(it.javaClass.name)
                        .setView(it)
                        .addAttributes(foundAttributes)
                        .build()

                    inflatedViewPublishSubject.onNext(themedView)
                }
            }
        }

        return result
    }

    private fun getResourceName(result: InflateResult, resId: Int): String {
        try {
            return result.context.resources.getResourceEntryName(resId)
        } catch (e: Exception) {

        }

        return "unknown"
    }

    private fun getThemedResourceId(result: InflateResult, scheme: String, name: String): Int {
        result.attrs!!.getAttributeValue(scheme, name)?.let {
            if (it.isNotEmpty() && it.startsWith("?")) {
                val resourceId = it.substring(1).toInt()
                if (getResourceName(result, resourceId).startsWith(THEME_PREFIX)) {
                    return resourceId
                }
            }
        }

        return 0
    }
}