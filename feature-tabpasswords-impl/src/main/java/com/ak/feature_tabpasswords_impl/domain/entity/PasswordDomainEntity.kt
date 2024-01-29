package com.ak.feature_tabpasswords_impl.domain.entity

import com.ak.core_repo_api.intefaces.password.PasswordRepoEntity
import com.ak.feature_tabpasswords_api.interfaces.PasswordFeatureEntity

class PasswordDomainEntity constructor(
    var passwordIdValue: Long?,
    var passwordNameValue: String,
    var passwordAvatarPathValue: String,
    var passwordContentValue: String,
    var passwordPinTimestampValue: Long?,
) : PasswordFeatureEntity, PasswordRepoEntity {

    constructor(name: String, avatarPath: String?, content: String) : this(
        null,
        name,
        avatarPath ?: "",
        content,
        null,
    )

    override fun getPasswordId(): Long? = passwordIdValue

    override fun getPasswordName(): String = passwordNameValue

    override fun getPasswordAvatarPath(): String = passwordAvatarPathValue

    override fun getPasswordContent(): String = passwordContentValue

    override fun getPasswordPinTimestamp(): Long? = passwordPinTimestampValue
}