package com.ak.passwordsaver.presentation.screens.passwords.logic

interface IDataBufferManager {
    fun copyStringData(label: String, data: String)
    fun copyStringData(data: String)
}