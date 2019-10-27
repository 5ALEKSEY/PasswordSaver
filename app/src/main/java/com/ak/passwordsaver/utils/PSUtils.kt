package com.ak.passwordsaver.utils

object PSUtils {
    fun getAbbreviationFormPasswordName(passwordName: String): String {
        val list = passwordName.split(" ")
        return when {
            list.isEmpty() -> ""
            list.size > 1 -> list[0].take(1) + list[1].take(1)
            list.size == 1 -> list[0].take(1)
            else -> ""
        }.toUpperCase()
    }
}