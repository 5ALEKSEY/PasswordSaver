package com.ak.app_theme.theme.selectors.util

data class TypedValue(val attributeId: Int, var index: Int,
                      val namespace: String, val name: String,
                      val type: Int, val data: Any) {
    companion object {
        const val NAMESPACE_ANDROID = "http://schemas.android.com/apk/res/android"
        const val NAMESPACE_APP = "http://schemas.android.com/apk/res-auto"

        const val TYPE_INT = 1
        const val TYPE_FLOAT = 2
        const val TYPE_DIMENSION = 3
        const val TYPE_COLOR = 4
        const val TYPE_DRAWABLE = 5
        const val TYPE_ATTRIBUTE = 6
        const val TYPE_COLOR_ATTRIBUTE = 7
        const val TYPE_DRAWABLE_ATTRIBUTE = 8
        const val TYPE_STRING = 9
    }

    fun getInt() = data as Int
    fun getFloat() = data as Float
    fun getDimension() = getFloat()
    fun getColor() = getInt()
    fun getResourceId() = getInt()
    fun getString() = data as String
}