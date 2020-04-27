package com.ak.base.utils

object PSUtils {
    fun getAbbreviationFormName(name: String): String {
        val list = name.split(" ")
        return when {
            list.isEmpty() -> ""
            list.size > 1 -> list[0].take(1) + list[1].take(1)
            list.size == 1 -> list[0].take(1)
            else -> ""
        }.toUpperCase()
    }
}