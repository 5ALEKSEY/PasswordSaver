package com.ak.feature_tabpasswords_api.interfaces

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface IPasswordsInteractor {
    fun getAllPasswords(): Flowable<List<PasswordFeatureEntity>>
    fun getPasswordById(passwordId: Long): Single<PasswordFeatureEntity>
    fun deletePasswordById(passwordId: Long): Single<Boolean>
    fun deletePasswordsByIds(passwordIds: List<Long>): Single<Boolean>
    fun addNewPassword(passwordFeatureEntity: PasswordFeatureEntity): Single<Boolean>
    fun addNewPasswords(passwordFeatureEntities: List<PasswordFeatureEntity>): Single<Boolean>
    fun updatePassword(passwordFeatureEntity: PasswordFeatureEntity): Single<Boolean>
    fun updatePasswords(passwordFeatureEntities: List<PasswordFeatureEntity>): Single<Boolean>
    fun pinPassword(passwordId: Long, pinnedTimestamp: Long): Single<Boolean>
    fun unpinPassword(passwordId: Long): Single<Boolean>
}