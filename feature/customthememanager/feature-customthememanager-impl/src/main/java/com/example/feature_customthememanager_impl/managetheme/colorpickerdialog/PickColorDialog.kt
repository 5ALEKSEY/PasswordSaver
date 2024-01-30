package com.example.feature_customthememanager_impl.managetheme.colorpickerdialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.applier.CustomThemeApplier
import com.ak.app_theme.theme.uicomponents.BaseThemeDialogFragment
import com.ak.feature_customthememanager_impl.R
import top.defaults.colorpicker.ColorPickerView

typealias ColorPickedCallback = (colorValue: Int) -> Unit

class PickColorDialog : BaseThemeDialogFragment() {

    var listener: ColorPickedCallback? = null

    private var root: View? = null
    private var dialogTitle: TextView? = null
    private var colorPickerView: ColorPickerView? = null
    private var cancelActionText: TextView? = null
    private var applyActionText: TextView? = null

    override fun applyTheme(theme: CustomTheme) {
        super.applyTheme(theme)
        CustomThemeApplier.applyBackgroundTint(theme, root, R.attr.themedPopupBackgroundColor)

        CustomThemeApplier.applyTextColor(
            theme,
            R.attr.themedPrimaryTextColor,
            dialogTitle,
            cancelActionText,
        )
        CustomThemeApplier.applyTextColor(theme, applyActionText, R.attr.themedAccentColor)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        isCancelable = false
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setWindowAnimations(R.style.popup_animation_medium)
        }

        return inflater.inflate(R.layout.pick_color_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (listener == null) {
            dismissAllowingStateLoss()
            return
        }

        // Bind usual views for theme applying
        root = view.findViewById(R.id.clPickColorDialogRoot)
        dialogTitle = view.findViewById(R.id.tvPickColorTitle)
        colorPickerView = view.findViewById<ColorPickerView>(R.id.cpvPickerWheel).apply {
            val initialColor = arguments?.getInt(INITIAL_COLOR_VALUE_ARG_KEY) ?: DEFAULT_INITIAL_COLOR
            setInitialColor(initialColor)
        }

        // Bind dialog action buttons listeners
        cancelActionText = view.findViewById<TextView>(R.id.tvCancelColorPickAction).apply {
            setOnClickListener { dismissAllowingStateLoss() }
        }
        applyActionText = view.findViewById<TextView>(R.id.tvPickColorAction).apply {
            setOnClickListener {
                listener?.invoke(colorPickerView?.color ?: DEFAULT_INITIAL_COLOR)
                dismissAllowingStateLoss()
            }
        }
    }

    companion object {
        private const val DIALOG_TAG = "pick_color_dialog"
        private const val INITIAL_COLOR_VALUE_ARG_KEY = "initial_color_value_arg_key"
        private const val DEFAULT_INITIAL_COLOR = Color.BLUE

        fun show(
            fragmentManager: FragmentManager,
            initialColorValue: Int,
            colorPickedCallback: ColorPickedCallback,
        ): PickColorDialog {
            return PickColorDialog().apply {
                this.listener = colorPickedCallback
                arguments = bundleOf(INITIAL_COLOR_VALUE_ARG_KEY to initialColorValue)
                show(fragmentManager, DIALOG_TAG)
            }
        }
    }
}