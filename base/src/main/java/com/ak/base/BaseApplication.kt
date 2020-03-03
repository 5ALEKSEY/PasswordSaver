package com.ak.base

import android.app.Application
import com.ak.base.di.BaseComponent

class BaseApplication : Application() {

    companion object {
        lateinit var appInstance: BaseApplication
    }

    private var daggerComponent: BaseComponent? = null

    fun getBaseDaggerComponent(): BaseComponent {
        if (daggerComponent != null) {
            return daggerComponent!!
        } else {
            throw IllegalStateException("Null BaseComponent in 'base' module")
        }
    }

    fun initWithComponent(daggerComponent: BaseComponent) {
        this.daggerComponent = daggerComponent
        this.daggerComponent?.inject(this)
    }

    fun clear() {
        daggerComponent = null
    }
}