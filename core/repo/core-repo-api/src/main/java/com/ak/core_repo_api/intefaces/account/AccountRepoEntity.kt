package com.ak.core_repo_api.intefaces.account

interface AccountRepoEntity {
    fun getAccountId(): Long?
    fun getAccountName(): String
    fun getAccountLogin(): String
    fun getAccountPassword(): String
    fun getAccountPinTimestamp(): Long?
}