package com.ak.passwordsaver.domain.passwords.usecase

import com.pvryan.easycrypt.ECResultListener
import com.pvryan.easycrypt.symmetric.ECSymmetric


class EncryptionUseCase {

    companion object {
        private const val ENCRYPTION_KEY = "16GHpodm8834NF036GLdeHG53"
    }

    private var mEncryptor: ECSymmetric = ECSymmetric()

    @Throws(Exception::class)
    fun encrypt(valueForEncrypt: String, onEncrypted: (encryptedValue: String) -> Unit, onError: (throwable: Throwable) -> Unit) {
        mEncryptor.encrypt(valueForEncrypt,
            ENCRYPTION_KEY, object : ECResultListener {
            override fun onFailure(message: String, e: Exception) {
                onError(e)
                throw e
            }

            override fun <T> onSuccess(result: T) {
                onEncrypted(result.toString())
            }
        })
    }

    @Throws(Exception::class)
    fun decrypt(valueForDecrypt: String, onDecrypted: (decryptedValue: String) -> Unit, onError: (throwable: Throwable) -> Unit) {
        mEncryptor.decrypt(valueForDecrypt,
            ENCRYPTION_KEY, object : ECResultListener {
            override fun onFailure(message: String, e: Exception) {
                onError(e)
                throw e
            }

            override fun <T> onSuccess(result: T) {
                onDecrypted(result.toString())
            }
        })
    }
}