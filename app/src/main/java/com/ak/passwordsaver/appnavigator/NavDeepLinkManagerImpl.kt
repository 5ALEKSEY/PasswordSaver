package com.ak.passwordsaver.appnavigator

import android.net.Uri
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import com.ak.base.navigation.NavDeepLinkDestination
import com.ak.base.navigation.NavDeepLinkManager
import com.ak.feature_tabsettings_impl.R
import javax.inject.Inject

class NavDeepLinkManagerImpl @Inject constructor() : NavDeepLinkManager {

    override fun navigate(
        navController: NavController,
        destination: NavDeepLinkDestination,
        shouldAnimate: Boolean,
    ) {
        val navigateRequest = NavDeepLinkRequest.Builder
            .fromUri(navigateTo(destination))
            .build()
        val animationOptions = if (shouldAnimate) PROJECT_NAVIGATE_ANIMATION_OPTIONS else null
        navController.navigate(
            request = navigateRequest,
            navOptions = animationOptions,
        )
    }

    private fun navigateTo(destination: NavDeepLinkDestination): Uri {
        return "$APP_HOST$APP_HOST_DIVIDER$NAVIGATE_ACTION$DEEP_LINK_SEPARATOR${destination.value}".toUri()
    }

    private companion object {
        const val NAVIGATE_ACTION = "navigate"
        const val APP_HOST = "ps"
        const val APP_HOST_DIVIDER = "://"
        const val DEEP_LINK_SEPARATOR = "/"

        val PROJECT_NAVIGATE_ANIMATION_OPTIONS = NavOptions.Builder()
            .setEnterAnim(R.anim.push_down_in)
            .setExitAnim(R.anim.push_down_out)
            .setPopEnterAnim(R.anim.push_up_out)
            .setPopExitAnim(R.anim.push_up_in)
            .build()
    }
}