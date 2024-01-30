package com.ak.feature_tabpasswords_api.interfaces

interface PasswordFeatureEntity {
    fun getPasswordId(): Long?
    fun getPasswordName(): String
    fun getPasswordAvatarPath(): String
    fun getPasswordContent(): String
    fun getPasswordPinTimestamp(): Long?
}