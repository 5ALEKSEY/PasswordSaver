package com.example.feature_backup_impl.sizebeautifier

import androidx.annotation.StringRes
import com.ak.feature_backup_impl.R

interface ISizeBeautifier {
    suspend fun beautifyBytes(sizeBytes: Long): BeautifiedSize
    suspend fun beautifiedBytesToString(sizeBytes: Long): String
    suspend fun beautifiedSizeToString(beautifiedSize: BeautifiedSize): String

    data class BeautifiedSize(
        val sizeValue: Double,
        val sizeFormat: Format,
    )

    enum class Format(
        val bytesValue: Long,
        @StringRes val localizedValue: Int,
    ) {
        BYTES(
            bytesValue = 1,
            localizedValue = R.string.format_bytes_value,
        ),
        KILOBYTES(
            bytesValue = 1024,
            localizedValue = R.string.format_kilobytes_value,
        ),
        MEGABYTES(
            bytesValue = 1024 * 1024,
            localizedValue = R.string.format_megabytes_value,
        ),
        GIGABYTES(
            bytesValue = 1024 * 1024 * 1024,
            localizedValue = R.string.format_gigabytes_value,
        ),
        ;

        companion object {
            fun formatsList() = listOf(
                BYTES,
                KILOBYTES,
                MEGABYTES,
                GIGABYTES,
            )
        }
    }
}