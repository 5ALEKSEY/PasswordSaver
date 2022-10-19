package com.ak.core_repo_api.intefaces

import kotlinx.coroutines.flow.Flow

interface IPasswordsRepository {
    fun getAllPasswords(): Flow<List<PasswordRepoEntity>>
    suspend fun getPasswordById(passwordId: Long): PasswordRepoEntity
    suspend fun deletePasswordsByIds(passwordIds: List<Long>): Boolean
    suspend fun addNewPasswords(passwordRepoEntities: List<PasswordRepoEntity>): Boolean
    suspend fun updatePasswords(passwordRepoEntities: List<PasswordRepoEntity>): Boolean
    suspend fun clearAll()
    suspend fun getPasswordsCount(): Int
    suspend fun pinPassword(passwordId: Long, pinnedTimestamp: Long): Boolean
    suspend fun unpinPassword(passwordId: Long): Boolean
}