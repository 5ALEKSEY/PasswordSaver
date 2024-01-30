package com.example.feature_backup_impl.fileshare

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.feature_backup_impl.R
import java.io.File
import javax.inject.Inject

class ShareFileManagerImpl @Inject constructor(
    private val appContext: Context,
    private val resourceManager: IResourceManager,
) : IShareFileManager {

    private val fileProviderAuthority get() = "${appContext.packageName}.fileprovider"

    override fun shareFileExternally(senderContext: Context, file: File) {
        val shareUri = file.uriForFile()
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_STREAM, shareUri)
            type = appContext.contentResolver.getType(shareUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        val intentChooser = Intent.createChooser(
            shareIntent,
            resourceManager.getString(R.string.share_dump_externally_chooser_title),
        )
        senderContext.startActivity(intentChooser)
    }

    private fun File.uriForFile() = FileProvider.getUriForFile(
        appContext,
        fileProviderAuthority,
        this,
    )
}