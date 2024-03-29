package com.ak.core_repo_api.intefaces.password

interface PasswordRepoEntity {
    fun getPasswordId(): Long?
    fun getPasswordName(): String
    fun getPasswordAvatarPath(): String
    fun getPasswordContent(): String
    fun getPasswordPinTimestamp(): Long?
}