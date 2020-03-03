package com.ak.base.di

import com.ak.base.BaseApplication
import com.ak.base.auth.SecurityPresenter
import dagger.Subcomponent

@Subcomponent
interface BaseComponent {

    fun inject(app: BaseApplication)
    fun inject(presenter: SecurityPresenter)
}