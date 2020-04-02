package com.ak.settings

import android.app.Application
import com.ak.settings.di.SettingsComponent

class SettingsApplication : Application() {

    companion object {
        lateinit var appInstance: SettingsApplication
    }

    private var daggerComponent: SettingsComponent? = null

    fun getSettingsDaggerComponent(): SettingsComponent {
        if (daggerComponent != null) {
            return daggerComponent!!
        } else {
            throw IllegalStateException("Null SettingsComponent in 'settings' module")
        }
    }

    fun initWithComponent(daggerComponent: SettingsComponent) {
        this.daggerComponent = daggerComponent
        this.daggerComponent?.inject(this)
    }

    fun clear() {
        daggerComponent = null
    }
}