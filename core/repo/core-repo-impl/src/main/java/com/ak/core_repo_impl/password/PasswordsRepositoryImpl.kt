package com.ak.core_repo_impl.password

import com.ak.core_repo_api.intefaces.password.IPasswordsRepository
import com.ak.core_repo_api.intefaces.password.PasswordRepoEntity
import com.ak.core_repo_impl.data.model.db.PSDatabase
import com.ak.core_repo_impl.data.model.db.entities.PasswordDBEntity
import com.ak.core_repo_impl.data.model.mapper.mapToPasswordDbEntitiesList
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PasswordsRepositoryImpl @Inject constructor(
    localStore: PSDatabase,
) : IPasswordsRepository {

    private val passwordsLocalStore = localStore.getPasswordsDao()

    override fun getAllPasswords(): Flow<List<PasswordRepoEntity>> {
        return passwordsLocalStore.getAllPasswords()
    }

    override suspend fun getPasswordById(passwordId: Long) = withContext(Dispatchers.IO) {
        passwordsLocalStore.getPasswordById(passwordId)
    }

    override suspend fun deletePasswordsByIds(passwordIds: List<Long>) = withContext(Dispatchers.IO) {
        val entitiesToDelete = passwordIds.map { PasswordDBEntity(it) }.toTypedArray()
        return@withContext passwordsLocalStore.deletePasswords(*entitiesToDelete) >= 0
    }

    override suspend fun addNewPasswords(passwordRepoEntities: List<PasswordRepoEntity>) = withContext(Dispatchers.IO) {
        val entitiesToAdd = passwordRepoEntities.mapToPasswordDbEntitiesList().toTypedArray()
        return@withContext passwordsLocalStore.insertNewPassword(*entitiesToAdd).isNotEmpty()
    }

    override suspend fun updatePasswords(passwordRepoEntities: List<PasswordRepoEntity>) = withContext(Dispatchers.IO) {
        val entitiesToUpdate = passwordRepoEntities.mapToPasswordDbEntitiesList().toTypedArray()
        return@withContext passwordsLocalStore.updatePasswords(*entitiesToUpdate) >= 0
    }

    override suspend fun clearAll() = withContext(Dispatchers.IO) {
        passwordsLocalStore.clearPasswords()
    }

    override suspend fun getPasswordsCount() = withContext(Dispatchers.IO) {
        passwordsLocalStore.getPasswordsCount()
    }

    override suspend fun pinPassword(passwordId: Long, pinnedTimestamp: Long) = withContext(Dispatchers.IO) {
        passwordsLocalStore.markPasswordAsPinned(passwordId, pinnedTimestamp) >= 0
    }

    override suspend fun unpinPassword(passwordId: Long) = withContext(Dispatchers.IO) {
        passwordsLocalStore.markPasswordAsUnpinned(passwordId) >= 0
    }
}