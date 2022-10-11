package com.ak.base.viewmodel

import androidx.lifecycle.ViewModel

interface IViewModelAssistedFactory<T : ViewModel> {
    fun create(): T
}