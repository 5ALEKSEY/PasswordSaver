package com.ak.feature_encryption_api.interfaces

interface IEncryptionManager {
    suspend fun encrypt(valueForEncrypt: String) : String
    suspend fun decrypt(valueForDecrypt: String) : String
}