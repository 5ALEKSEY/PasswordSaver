package com.ak.passwordsaver.presentation.screens.passwords.logic

import com.ak.passwordsaver.model.db.PSDatabase
import com.ak.passwordsaver.model.db.entities.PasswordDBEntity
import com.ak.passwordsaver.presentation.base.encryption.EncryptionUseCase
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PasswordsListInteractorImpl @Inject constructor(
    val mPsDatabase: PSDatabase
) : PasswordsListInteractor {

    private val mEncryptionUseCase: EncryptionUseCase = EncryptionUseCase()

    override fun getAndListenAllPasswords(): Flowable<List<PasswordDBEntity>> {
        return mPsDatabase.getPasswordsDao().getAllPasswords()
            .subscribeOn(Schedulers.io())
            .map { encryptedPasswords ->
                val decryptedPasswords = arrayListOf<PasswordDBEntity>()
                encryptedPasswords.forEach {
                    decryptedPasswords
                }
                return@map decryptedPasswords
            }
    }

    private fun getInvokedDecryptionUseCase(encryptedList: List<PasswordDBEntity>) =
            Observable.fromIterable(encryptedList)
                .map { encryptedEntity -> encryptedEntity.passwordContent = mEncryptionUseCase }
}