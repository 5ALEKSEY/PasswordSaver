package com.ak.feature_encryption_impl.encryption

import com.ak.feature_encryption_api.interfaces.IEncryptionManager
import com.ak.feature_encryption_impl.BuildConfig
import com.pvryan.easycrypt.ECResultListener
import com.pvryan.easycrypt.symmetric.ECSymmetric
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class EncryptionManagerImpl @Inject constructor() : IEncryptionManager {

    override suspend fun encrypt(valueForEncrypt: String) = suspendCoroutine<String> { continuation ->
        ECSymmetric().encrypt(
            valueForEncrypt,
            BuildConfig.ENCRYPTION_KEY,
            object : ECResultListener {
                override fun onFailure(message: String, e: Exception) {
                    continuation.resumeWithException(e)
                }

                override fun <T> onSuccess(result: T) {
                    continuation.resume(result.toString())
                }
            },
        )
    }

    override suspend fun decrypt(valueForDecrypt: String) = suspendCoroutine<String> { continuation ->
        ECSymmetric().decrypt(
            valueForDecrypt,
            BuildConfig.ENCRYPTION_KEY,
            object : ECResultListener {
                override fun onFailure(message: String, e: Exception) {
                    continuation.resumeWithException(e)
                }

                override fun <T> onSuccess(result: T) {
                    continuation.resume(result.toString())
                }
            },
        )
    }
}