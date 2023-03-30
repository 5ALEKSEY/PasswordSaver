package com.ak.base.navigation

import androidx.navigation.NavController

interface NavDeepLinkManager {
    fun navigate(
        navController: NavController,
        destination: NavDeepLinkDestination,
        shouldAnimate: Boolean = true,
    )

    interface Provider {
        fun provideNavDeepLinkManager(): NavDeepLinkManager
    }
}

sealed class NavDeepLinkDestination(val value: String) {
    object BackupInfo : NavDeepLinkDestination("backup_info")
    object AddNewPassword : NavDeepLinkDestination("add_new_password")
    object AddNewAccount : NavDeepLinkDestination("add_new_account")
}