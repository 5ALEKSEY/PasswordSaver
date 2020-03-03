package com.ak.domain.di

import com.ak.domain.DomainApplication
import dagger.Subcomponent

@Subcomponent
interface DomainComponent {

    fun inject(app: DomainApplication)
}