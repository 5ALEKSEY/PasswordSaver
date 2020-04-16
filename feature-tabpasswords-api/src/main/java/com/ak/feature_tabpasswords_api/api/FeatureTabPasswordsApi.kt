package com.ak.feature_tabpasswords_api.api

import com.ak.feature_tabpasswords_api.interfaces.IPasswordsInteractor

interface FeatureTabPasswordsApi {
    fun providePasswordsInteractor(): IPasswordsInteractor
}