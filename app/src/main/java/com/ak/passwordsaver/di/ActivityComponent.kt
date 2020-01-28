package com.ak.passwordsaver.di

import com.ak.passwordsaver.di.modules.ActivityModule
import com.ak.passwordsaver.di.scopes.ActivityScope
import dagger.Component

@Component(
    modules = [ActivityModule::class],
    dependencies = [AppComponent::class]
)
@ActivityScope
interface ActivityComponent