package com.ak.passwordsaver

import com.facebook.stetho.Stetho

class DebugPSApplication : PSApplication() {

    override fun onCreate() {
        super.onCreate()
        // Init Stetho
        Stetho.initializeWithDefaults(this)
    }
}