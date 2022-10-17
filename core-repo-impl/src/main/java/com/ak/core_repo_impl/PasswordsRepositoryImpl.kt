package com.ak.core_repo_impl

import com.ak.core_repo_api.intefaces.IPasswordsRepository
import com.ak.core_repo_api.intefaces.PasswordRepoEntity
import com.ak.core_repo_impl.data.model.db.PSDatabase
import com.ak.core_repo_impl.data.model.db.entities.PasswordDBEntity
import com.ak.core_repo_impl.data.model.mapper.mapToPasswordDbEntitiesList
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PasswordsRepositoryImpl @Inject constructor(
    private val passwordsLocalStore: PSDatabase,
) : IPasswordsRepository {

    override fun getAllPasswords(): Flow<List<PasswordRepoEntity>> {
        return passwordsLocalStore.getPasswordsDao().getAllPasswords()
    }

    override suspend fun getPasswordById(passwordId: Long) = withContext(Dispatchers.IO) {
        passwordsLocalStore.getPasswordsDao().getPasswordById(passwordId)
    }

    override suspend fun deletePasswordsByIds(passwordIds: List<Long>) = withContext(Dispatchers.IO) {
        val entitiesToDelete = passwordIds.map { PasswordDBEntity(it) }.toTypedArray()
        return@withContext passwordsLocalStore.getPasswordsDao().deletePasswords(*entitiesToDelete) >= 0
    }

    override suspend fun addNewPasswords(passwordRepoEntities: List<PasswordRepoEntity>) = withContext(Dispatchers.IO) {
        val entitiesToAdd = passwordRepoEntities.mapToPasswordDbEntitiesList().toTypedArray()
        return@withContext passwordsLocalStore.getPasswordsDao().insertNewPassword(*entitiesToAdd).isNotEmpty()
    }

    override suspend fun updatePasswords(passwordRepoEntities: List<PasswordRepoEntity>) = withContext(Dispatchers.IO) {
        val entitiesToUpdate = passwordRepoEntities.mapToPasswordDbEntitiesList().toTypedArray()
        return@withContext passwordsLocalStore.getPasswordsDao().updatePasswords(*entitiesToUpdate) >= 0
    }

    override fun clearAll(): Single<Boolean> =
        Single.fromCallable {
            passwordsLocalStore.getPasswordsDao().clearPasswords()
            return@fromCallable true
        }.subscribeOn(Schedulers.io())

    override fun getPasswordsCount() = passwordsLocalStore.getPasswordsDao().getPasswordsCount()

    override suspend fun pinPassword(passwordId: Long, pinnedTimestamp: Long) = withContext(Dispatchers.IO) {
        passwordsLocalStore.getPasswordsDao().markPasswordAsPinned(passwordId, pinnedTimestamp) >= 0
    }

    override suspend fun unpinPassword(passwordId: Long) = withContext(Dispatchers.IO) {
        passwordsLocalStore.getPasswordsDao().markPasswordAsUnpinned(passwordId) >= 0
    }
}