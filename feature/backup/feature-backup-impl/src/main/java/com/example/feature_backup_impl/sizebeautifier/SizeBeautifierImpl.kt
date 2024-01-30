package com.example.feature_backup_impl.sizebeautifier

import com.ak.core_repo_api.intefaces.IResourceManager
import com.example.feature_backup_impl.sizebeautifier.ISizeBeautifier.BeautifiedSize
import java.text.DecimalFormat
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SizeBeautifierImpl @Inject constructor(
    private val resourceManager: IResourceManager,
): ISizeBeautifier {

    override suspend fun beautifyBytes(sizeBytes: Long) = withContext(Dispatchers.Default) {
        val formats = ISizeBeautifier.Format.formatsList()

        for (format in formats) {
            val sizeCandidate = sizeBytes.toDouble() / format.bytesValue.toDouble()
            if (sizeCandidate <= HUMAN_READABLE_SIZE) {
                return@withContext BeautifiedSize(sizeCandidate, format)
            }
        }

        val maxFormat = formats.last()
        return@withContext BeautifiedSize(
            sizeBytes.toDouble() / maxFormat.bytesValue.toDouble(),
            maxFormat,
        )
    }

    override suspend fun beautifiedBytesToString(sizeBytes: Long) = withContext(Dispatchers.Default) {
        beautifiedSizeToString(beautifyBytes(sizeBytes = sizeBytes))
    }

    override suspend fun beautifiedSizeToString(beautifiedSize: BeautifiedSize): String {
        return "${beautifiedSize.formattedSize()} ${beautifiedSize.localizedFormat()}"
    }

    private fun BeautifiedSize.formattedSize() = SIZE_PATTERN.format(sizeValue)

    private fun BeautifiedSize.localizedFormat() = resourceManager.getString(sizeFormat.localizedValue)

    private companion object {
        const val HUMAN_READABLE_SIZE = 1000
        val SIZE_PATTERN = DecimalFormat("0.00")
    }
}