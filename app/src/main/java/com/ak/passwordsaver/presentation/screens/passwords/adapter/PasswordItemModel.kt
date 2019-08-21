package com.ak.passwordsaver.presentation.screens.passwords.adapter

data class PasswordItemModel(
    val passwordId: Long,
    val name: String,
    val photoUrl: String,
    val password: String
)