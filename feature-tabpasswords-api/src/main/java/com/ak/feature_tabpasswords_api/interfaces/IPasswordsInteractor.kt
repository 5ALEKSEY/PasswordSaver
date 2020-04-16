package com.ak.feature_tabpasswords_api.interfaces

import io.reactivex.Flowable
import io.reactivex.Single

interface IPasswordsInteractor {
    fun getAllPasswords(): Flowable<List<PasswordFeatureEntity>>
    fun getPasswordById(passwordId: Long): Single<PasswordFeatureEntity>
    fun deletePasswordById(passwordId: Long): Single<Boolean>
    fun deletePasswordsByIds(passwordIds: List<Long>): Single<Boolean>
    fun addNewPassword(passwordFeatureEntity: PasswordFeatureEntity): Single<Boolean>
    fun addNewPasswords(passwordFeatureEntities: List<PasswordFeatureEntity>): Single<Boolean>
    fun updatePassword(passwordFeatureEntity: PasswordFeatureEntity): Single<Boolean>
    fun updatePasswords(passwordFeatureEntities: List<PasswordFeatureEntity>): Single<Boolean>
}