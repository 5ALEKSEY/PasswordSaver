package com.example.feature_backup_impl.timeneautifier

interface ITimeBeautifier {
    suspend fun beautifyTimestamp(time: Long): String
}