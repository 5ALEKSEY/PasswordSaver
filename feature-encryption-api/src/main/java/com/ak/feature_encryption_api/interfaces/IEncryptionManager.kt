package com.ak.feature_encryption_api.interfaces

interface IEncryptionManager {
    @Throws(Exception::class)
    fun encrypt(valueForEncrypt: String, onEncrypted: (encryptedValue: String) -> Unit, onError: (throwable: Throwable) -> Unit)
    @Throws(Exception::class)
    fun decrypt(valueForDecrypt: String, onDecrypted: (decryptedValue: String) -> Unit, onError: (throwable: Throwable) -> Unit)
    suspend fun encrypt(valueForEncrypt: String) : String
    suspend fun decrypt(valueForDecrypt: String) : String
}