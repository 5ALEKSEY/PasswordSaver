package com.ak.feature_tabaccounts_api.interfaces

interface AccountFeatureEntity {
    fun getAccountId(): Long?
    fun getAccountName(): String
    fun getAccountLogin(): String
    fun getAccountPassword(): String
}