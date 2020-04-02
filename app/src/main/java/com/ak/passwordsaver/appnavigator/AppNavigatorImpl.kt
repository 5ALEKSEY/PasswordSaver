package com.ak.passwordsaver.appnavigator

import android.content.Context
import android.widget.Toast
import javax.inject.Inject

class AppNavigatorImpl @Inject constructor(
    private val applicationContext: Context
) : IAppNavigator {

    override fun testNavigation() {
        Toast.makeText(applicationContext, "testNavigation", Toast.LENGTH_SHORT).show()
    }
}