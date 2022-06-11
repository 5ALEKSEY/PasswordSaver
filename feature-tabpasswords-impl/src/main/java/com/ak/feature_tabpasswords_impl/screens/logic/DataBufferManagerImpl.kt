package com.ak.feature_tabpasswords_impl.screens.logic

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import javax.inject.Inject

// TODO: move to common module
class DataBufferManagerImpl @Inject constructor(
    private val context: Context
) : IDataBufferManager {

    companion object {
        private const val COPIED_CLIP_DATA_LABEL = "ps_clip_label"
    }

    private val mClipboardManager: ClipboardManager by lazy {
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    override fun copyStringData(label: String, data: String) {
        mClipboardManager.setPrimaryClip(ClipData.newPlainText(label, data))
    }

    override fun copyStringData(data: String) {
        copyStringData(COPIED_CLIP_DATA_LABEL, data)
    }
}