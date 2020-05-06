package com.ak.core_repo_api.intefaces

import androidx.annotation.StringRes

interface IResourceManager {
    fun getString(@StringRes stringResId: Int): String
    fun getString(@StringRes stringResId: Int, vararg formatArgs: Any): String
}