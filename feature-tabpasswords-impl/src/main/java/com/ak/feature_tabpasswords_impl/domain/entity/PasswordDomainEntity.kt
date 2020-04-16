package com.ak.feature_tabpasswords_impl.domain.entity

import com.ak.core_repo_api.intefaces.PasswordRepoEntity
import com.ak.feature_tabpasswords_api.interfaces.PasswordFeatureEntity

class PasswordDomainEntity(
    var passwordIdValue: Long?,
    var passwordNameValue: String,
    var passwordAvatarPathValue: String,
    var passwordContentValue: String
) : PasswordFeatureEntity, PasswordRepoEntity {

    constructor(name: String, avatarPath: String?, content: String) : this(
        null,
        name,
        avatarPath ?: "",
        content
    )

    override fun getPasswordId(): Long? = passwordIdValue

    override fun getPasswordName(): String = passwordNameValue

    override fun getPasswordAvatarPath(): String = passwordAvatarPathValue

    override fun getPasswordContent(): String = passwordContentValue
}