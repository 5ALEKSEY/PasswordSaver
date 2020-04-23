package com.ak.feature_tabaccounts_api.api

import com.ak.feature_tabaccounts_api.interfaces.IAccountsInteractor

interface FeatureTabAccountsApi {
    fun provideAccountsInteractor(): IAccountsInteractor
}