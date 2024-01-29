package com.ak.passwordsaver

class DebugPSApplication : PSApplication() {

    override fun onCreate() {
        super.onCreate()
        val a = 1
        // Init Stetho
//        Stetho.initializeWithDefaults(this)
    }
}