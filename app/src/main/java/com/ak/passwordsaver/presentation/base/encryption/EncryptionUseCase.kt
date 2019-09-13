package com.ak.passwordsaver.presentation.base.encryption

import com.pvryan.easycrypt.ECResultListener
import com.pvryan.easycrypt.symmetric.ECSymmetric


class EncryptionUseCase {

    companion object {
        private const val ENCRYPTION_KEY = "16GHpodm8834NF036GLdeHG53"
    }

    private var mEncryptor: ECSymmetric = ECSymmetric()

    @Throws(Exception::class)
    fun encrypt(valueForEncrypt: String, onEncrypted: (encryptedValue: String) -> Unit) {
        mEncryptor.encrypt(valueForEncrypt, ENCRYPTION_KEY, object : ECResultListener {
            override fun onFailure(message: String, e: Exception) {
                throw e
            }

            override fun <T> onSuccess(result: T) {
                onEncrypted.invoke(result.toString())
            }
        })
    }

    @Throws(Exception::class)
    fun decrypt(valueForDecrypt: String, onDecrypted: (decryptedValue: String) -> Unit) {
        mEncryptor.decrypt(valueForDecrypt, ENCRYPTION_KEY, object : ECResultListener {
            override fun onFailure(message: String, e: Exception) {
                throw e
            }

            override fun <T> onSuccess(result: T) {
                onDecrypted.invoke(result.toString())
            }
        })
    }
}