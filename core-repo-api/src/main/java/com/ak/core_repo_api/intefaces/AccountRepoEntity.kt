package com.ak.core_repo_api.intefaces

interface AccountRepoEntity {
    fun getAccountId(): Long?
    fun getAccountName(): String
    fun getAccountAvatarPath(): String
    fun getAccountLogin(): String
    fun getAccountPassword(): String
}