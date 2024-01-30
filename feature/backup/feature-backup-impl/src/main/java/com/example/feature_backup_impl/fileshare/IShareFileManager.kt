package com.example.feature_backup_impl.fileshare

import android.content.Context
import java.io.File

interface IShareFileManager {
    fun shareFileExternally(senderContext: Context, file: File)
}