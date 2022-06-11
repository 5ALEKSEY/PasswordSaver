package com.ak.app_theme.theme.selectors.util

import android.content.Context
import android.util.AttributeSet
import com.ak.app_theme.R
import com.ak.app_theme.theme.CustomTheme

object TypedUtils {
    private fun getResourceName(context: Context, resId: Int): String {
        return try {
            context.resources.getResourceName(resId)
        } catch (e: Exception) {
            "unknown"
        }
    }

    private fun getResourceTypeName(context: Context, resId: Int): String {
        return try {
            context.resources.getResourceTypeName(resId)
        } catch (e: Exception) {
            "unknown"
        }
    }

    private fun getResourceIndentifier(context: Context, resName: String, resType:String): Int {
        return try {
            context.resources.getIdentifier(resName, resType, context.packageName)
        } catch (e: Exception) {
            -1
        }
    }

    private fun parseAttributeName(context: Context, attrResId: Int): String {
        val resName =
            getResourceName(
                context,
                attrResId
            )
        return resName.split("/").getOrElse(1) { resName }
    }

    private fun parseAttributeNamespace(context: Context, attrResId: Int): String {
        val resName =
            getResourceName(
                context,
                attrResId
            )
        if (resName.startsWith("android")) {
            return TypedValue.NAMESPACE_ANDROID
        }
        return TypedValue.NAMESPACE_APP
    }

    private fun getThemeAttributeType(theme: CustomTheme?, attr: Int) = when {
        theme != null && theme.isColor(attr) -> TypedValue.TYPE_COLOR_ATTRIBUTE
        theme != null && theme.isDrawable(attr) -> TypedValue.TYPE_DRAWABLE_ATTRIBUTE
        attr == R.attr.auto -> TypedValue.TYPE_COLOR_ATTRIBUTE
        else -> TypedValue.TYPE_ATTRIBUTE
    }

    private fun findAttributeReferences(context: Context, set: AttributeSet,
                                        theme: CustomTheme? = null): HashMap<Int, TypedValue> {
        val foundAttributes = hashMapOf<Int, TypedValue>()

        for (index in 0 until set.attributeCount) {
            val value = set.getAttributeValue(index)
            if (value?.startsWith("?") == true) {
                val name = set.getAttributeName(index)
                val nameResId = set.getAttributeNameResource(index)
                val namespace =
                    parseAttributeNamespace(
                        context,
                        nameResId
                    )
                value.substring(1).toIntOrNull()?.let { attrValue ->
                    val type =
                        getThemeAttributeType(
                            theme,
                            attrValue
                        )
                    foundAttributes[nameResId] = TypedValue(nameResId, -1, namespace, name, type, attrValue)
                }
            }
        }

        return foundAttributes
    }

    @JvmStatic
    fun obtainStyledAttributes(context: Context, set: AttributeSet?,
                               attrs: IntArray, theme: CustomTheme? = null): List<TypedValue>? {
        if (set == null || set.attributeCount == 0) {
            return null
        }

        val typedArray = context.obtainStyledAttributes(set, attrs) ?: return null
        if (typedArray.indexCount == 0) {
            typedArray.recycle()
            return null
        }

        val attrReferences =
            findAttributeReferences(
                context,
                set,
                theme
            )
        val result = mutableListOf<TypedValue>()

        for (position in 0 until typedArray.indexCount) {
            val index = typedArray.getIndex(position)
            val attrId = attrs[index]

            if (attrReferences.containsKey(attrId)) {
                attrReferences[attrId]?.let {
                    it.index = index
                    result.add(it)
                }
                continue
            }

            val resId = typedArray.getResourceId(index, -1)
            parseTypedValue(
                context,
                theme,
                attrId,
                index,
                resId,
                typedArray.peekValue(index)
            )?.let {
                result.add(it)
            }
        }

        typedArray.recycle()

        return result
    }

    @JvmStatic
    fun obtainStyledAttributes(theme: CustomTheme, styleResId: Int, attrs: IntArray): List<TypedValue>? {
        theme.context.obtainStyledAttributes(styleResId, attrs)?.let { typedArray ->
            val result = mutableListOf<TypedValue>()

            for (position in 0 until typedArray.indexCount) {
                val index = typedArray.getIndex(position)
                val resId = typedArray.getResourceId(index, -1)

                parseTypedValue(
                    theme.context,
                    theme,
                    attrs[index],
                    index,
                    resId,
                    typedArray.peekValue(index)
                )?.let {
                    result.add(it)
                }
            }

            typedArray.recycle()
            return result
        }

        return null
    }

    private fun parseTypedValue(context: Context, theme: CustomTheme?,
                                attrId: Int, index:Int, resId: Int,
                                value: android.util.TypedValue?) : TypedValue? {
        val name =
            parseAttributeName(
                context,
                attrId
            )
        val namespace =
            parseAttributeNamespace(
                context,
                attrId
            )

        return when (value?.type) {
            android.util.TypedValue.TYPE_INT_DEC -> {
                TypedValue(attrId, index, namespace, name, TypedValue.TYPE_INT, value.data)
            }
            android.util.TypedValue.TYPE_FLOAT -> {
                TypedValue(attrId, index, namespace, name, TypedValue.TYPE_FLOAT, value.float)
            }
            android.util.TypedValue.TYPE_DIMENSION -> {
                val displayMetrics = context.resources.displayMetrics
                val dpValue = value.getDimension(displayMetrics) / displayMetrics.density
                TypedValue(attrId, index, namespace, name, TypedValue.TYPE_DIMENSION, dpValue)
            }
            android.util.TypedValue.TYPE_ATTRIBUTE -> {
                val type =
                    getThemeAttributeType(
                        theme,
                        value.data
                    )
                TypedValue(attrId, index, namespace, name, type, value.data)
            }
            android.util.TypedValue.TYPE_INT_COLOR_RGB4,
            android.util.TypedValue.TYPE_INT_COLOR_RGB8,
            android.util.TypedValue.TYPE_INT_COLOR_ARGB4,
            android.util.TypedValue.TYPE_INT_COLOR_ARGB8 -> {
                TypedValue(attrId, index, namespace, name, TypedValue.TYPE_COLOR, value.data)
            }
            android.util.TypedValue.TYPE_STRING -> {
                if (resId != -1 && getResourceTypeName(
                        context,
                        resId
                    ) == "drawable") {
                    TypedValue(attrId, index, namespace, name, TypedValue.TYPE_DRAWABLE, resId)
                } else {
                    val stringValue = value.string
                    when {
                        stringValue.startsWith("?") -> {
                            val attrName = stringValue.substring(1)
                            val id =
                                getResourceIndentifier(
                                    context,
                                    attrName,
                                    "attr"
                                )
                            if (id != -1) {
                                val type =
                                    getThemeAttributeType(
                                        theme,
                                        id
                                    )
                                TypedValue(attrId, index, namespace, name, type, id)
                            } else {
                                TypedValue(attrId, index, namespace, name, TypedValue.TYPE_STRING, stringValue)
                            }
                        }
                        stringValue == "auto" -> {
                            TypedValue(attrId, index, namespace, name, TypedValue.TYPE_COLOR_ATTRIBUTE, R.attr.auto)
                        }
                        else -> {
                            TypedValue(attrId, index, namespace, name, TypedValue.TYPE_STRING, stringValue)
                        }
                    }
                }
            }
            else -> null
        }
    }
}