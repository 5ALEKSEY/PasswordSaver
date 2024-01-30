package com.ak.core_repo_impl

import android.content.Context
import com.ak.core_repo_api.intefaces.IResourceManager
import javax.inject.Inject

class ResourceManagerImpl @Inject constructor(
    private val appContext: Context
) : IResourceManager {

    override fun getString(stringResId: Int): String {
        return appContext.getString(stringResId)
    }

    override fun getString(stringResId: Int, vararg formatArgs: Any): String {
        return appContext.getString(stringResId, *formatArgs)
    }
}