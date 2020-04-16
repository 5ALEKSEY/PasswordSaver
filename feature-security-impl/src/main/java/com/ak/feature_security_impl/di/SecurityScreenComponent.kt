package com.ak.feature_security_impl.di

import com.ak.feature_security_impl.auth.SecurityPresenter
import dagger.Subcomponent

@Subcomponent
@SecurityScreenScope
interface SecurityScreenComponent {
    fun injectSecurityPresenter(presenter: SecurityPresenter)
}