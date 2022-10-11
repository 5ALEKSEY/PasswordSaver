package com.ak.base.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.children
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.applier.CustomThemeApplier
import com.ak.base.R
import com.ak.base.extensions.toColorStateList
import com.google.android.material.textfield.TextInputLayout

class PsThemedTextInputLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputLayout(context, attrs), CustomTheme.Support {

    override fun applyTheme(theme: CustomTheme) {
        val hintColor = theme.getColor(R.attr.themedPrimaryTextHintColor).toColorStateList()
        val errorColor = theme.getColor(R.attr.themedErrorColor).toColorStateList()

        defaultHintTextColor = hintColor
        hintTextColor = hintColor
        setErrorTextColor(errorColor)

        val innerView = getChildAt(0)
        if (innerView is ViewGroup) {
            applyThemeForInnerView(innerView, theme)
        }
    }

    private fun applyThemeForInnerView(innerView: ViewGroup, theme: CustomTheme) {
        innerView.children.filterIsInstance<EditText>()
            .filter { it.compoundDrawables.isNotEmpty() }
            .forEach { editText ->
                CustomThemeApplier.applyCompoundDrawablesTint(theme, editText, R.attr.themedPrimaryColor)
                editText.textCursorDrawable?.let {
                    editText.textCursorDrawable = CustomThemeApplier.tint(
                        theme,
                        it,
                        R.attr.themedPrimaryColor,
                    )
                }
                // TODO: think how to set correct drawable here to match all states
//                editText.backgroundTintList = theme.getColor(R.attr.themedPrimaryColor).toColorStateList()
            }
    }
}