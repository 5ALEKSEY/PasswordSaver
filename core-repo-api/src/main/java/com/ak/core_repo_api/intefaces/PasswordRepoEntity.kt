package com.ak.core_repo_api.intefaces

interface PasswordRepoEntity {
    fun getPasswordId(): Long?
    fun getPasswordName(): String
    fun getPasswordAvatarPath(): String
    fun getPasswordContent(): String
}