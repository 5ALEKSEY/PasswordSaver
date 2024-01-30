package com.ak.feature_tabpasswords_impl.domain

import com.ak.core_repo_api.intefaces.password.IPasswordsRepository
import com.ak.feature_encryption_api.interfaces.IEncryptionManager
import com.ak.feature_tabpasswords_api.interfaces.IPasswordsInteractor
import com.ak.feature_tabpasswords_api.interfaces.PasswordFeatureEntity
import com.ak.feature_tabpasswords_impl.domain.entity.PasswordDomainEntity
import com.ak.feature_tabpasswords_impl.domain.entity.mapFeatureToDomainEntitiesList
import com.ak.feature_tabpasswords_impl.domain.entity.mapRepoToDomainEntitiesList
import com.ak.feature_tabpasswords_impl.domain.entity.mapToDomainEntity
import com.ak.feature_tabpasswords_impl.domain.usecase.PasswordDataCheckUseCase
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PasswordsInteractorImpl @Inject constructor(
    private val passwordsRepository: IPasswordsRepository,
    private val encryptionManager: IEncryptionManager,
) : IPasswordsInteractor {

    private val passwordDataCheckUseCase: PasswordDataCheckUseCase = PasswordDataCheckUseCase()

    override fun getAllPasswords(): Flow<List<PasswordFeatureEntity>> {
        return passwordsRepository.getAllPasswords().map {
            decryptPasswordEntities(it.mapRepoToDomainEntitiesList())
        }
    }

    override suspend fun getPasswordById(passwordId: Long) = withContext(Dispatchers.IO) {
        passwordsRepository.getPasswordById(passwordId)
            .mapToDomainEntity()
            .apply { passwordContentValue = encryptionManager.decrypt(passwordContentValue) }
    }

    override suspend fun deletePasswordById(passwordId: Long) = withContext(Dispatchers.IO) {
        deletePasswordsByIds(listOf(passwordId))
    }

    override suspend fun deletePasswordsByIds(passwordIds: List<Long>) = withContext(Dispatchers.IO) {
        passwordsRepository.deletePasswordsByIds(passwordIds)
    }

    override suspend fun addNewPassword(passwordFeatureEntity: PasswordFeatureEntity) = withContext(Dispatchers.IO) {
        addNewPasswords(listOf(passwordFeatureEntity))
    }

    override suspend fun addNewPasswords(passwordFeatureEntities: List<PasswordFeatureEntity>) = withContext(Dispatchers.IO) {
        val domainEntities = passwordFeatureEntities.mapFeatureToDomainEntitiesList()

        // verify password entities
        domainEntities.forEach {
            passwordDataCheckUseCase.verifyPasswordData(it.passwordNameValue, it.passwordContentValue)
        }

        val domainEntitiesEncrypted = encryptPasswordEntities(domainEntities)
        return@withContext passwordsRepository.addNewPasswords(domainEntitiesEncrypted)
    }

    override suspend fun updatePassword(passwordFeatureEntity: PasswordFeatureEntity) = withContext(Dispatchers.IO) {
        updatePasswords(listOf(passwordFeatureEntity))
    }

    override suspend fun updatePasswords(passwordFeatureEntities: List<PasswordFeatureEntity>) = withContext(Dispatchers.IO) {
        val domainEntities = passwordFeatureEntities.mapFeatureToDomainEntitiesList()

        // verify password entities
        domainEntities.forEach {
            passwordDataCheckUseCase.verifyPasswordData(it.passwordNameValue, it.passwordContentValue)
        }

        val domainEntitiesEncrypted = encryptPasswordEntities(domainEntities)
        return@withContext passwordsRepository.updatePasswords(domainEntitiesEncrypted)
    }

    override suspend fun pinPassword(passwordId: Long, pinnedTimestamp: Long) = withContext(Dispatchers.IO) {
        passwordsRepository.pinPassword(passwordId, pinnedTimestamp)
    }

    override suspend fun unpinPassword(passwordId: Long) = withContext(Dispatchers.IO) {
        passwordsRepository.unpinPassword(passwordId)
    }

    private suspend fun decryptPasswordEntities(
        entities: List<PasswordDomainEntity>,
    ) = withContext(Dispatchers.IO) {
        entities.mapToDecryptionCoroutines().awaitAll()
    }

    private suspend fun encryptPasswordEntities(
        entities: List<PasswordDomainEntity>,
    ) = withContext(Dispatchers.IO) {
        entities.mapToEncryptionCoroutines().awaitAll()
    }

    private suspend fun List<PasswordDomainEntity>.mapToDecryptionCoroutines() = withContext(Dispatchers.IO) {
        map {
            async { it.apply { passwordContentValue = encryptionManager.decrypt(passwordContentValue) } }
        }
    }

    private suspend fun List<PasswordDomainEntity>.mapToEncryptionCoroutines() = withContext(Dispatchers.IO) {
        map {
            async { it.apply { passwordContentValue = encryptionManager.encrypt(passwordContentValue) } }
        }
    }
}