package com.ak.feature_tabpasswords_api.interfaces

import kotlinx.coroutines.flow.Flow

interface IPasswordsInteractor {
    fun getAllPasswords(): Flow<List<PasswordFeatureEntity>>
    suspend fun getPasswordById(passwordId: Long): PasswordFeatureEntity
    suspend fun deletePasswordById(passwordId: Long): Boolean
    suspend fun deletePasswordsByIds(passwordIds: List<Long>): Boolean
    suspend fun addNewPassword(passwordFeatureEntity: PasswordFeatureEntity): Boolean
    suspend fun addNewPasswords(passwordFeatureEntities: List<PasswordFeatureEntity>): Boolean
    suspend fun updatePassword(passwordFeatureEntity: PasswordFeatureEntity): Boolean
    suspend fun updatePasswords(passwordFeatureEntities: List<PasswordFeatureEntity>): Boolean
    suspend fun pinPassword(passwordId: Long, pinnedTimestamp: Long): Boolean
    suspend fun unpinPassword(passwordId: Long): Boolean
}