package com.ak.base.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Provider

open class BaseViewModelFactory constructor(
    private val viewModels: MutableMap<Class<out ViewModel>, IViewModelAssistedFactory<out ViewModel>>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModels[modelClass]?.create() as? T
            ?: throw IllegalArgumentException("$modelClass not found in ViewModelFactory list")
    }
}