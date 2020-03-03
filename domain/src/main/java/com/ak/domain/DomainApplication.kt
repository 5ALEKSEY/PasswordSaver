package com.ak.domain

import android.app.Application
import com.ak.domain.di.DomainComponent

class DomainApplication : Application() {

    companion object {
        lateinit var appInstance: DomainApplication
    }

    private var daggerComponent: DomainComponent? = null

    fun getDomainDaggerComponent(): DomainComponent {
        if (daggerComponent != null) {
            return daggerComponent!!
        } else {
            throw IllegalStateException("Null DomainComponent in 'domain' module")
        }
    }

    fun initWithComponent(daggerComponent: DomainComponent) {
        this.daggerComponent = daggerComponent
        this.daggerComponent?.inject(this)
    }

    fun clear() {
        daggerComponent = null
    }
}