package com.ak.base.ui.dialog

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.applier.CustomThemeApplier
import com.ak.app_theme.theme.uicomponents.BaseThemeDialogFragment
import com.ak.base.R
import com.ak.base.extensions.setVisibilityInvisible
import kotlinx.android.synthetic.main.layout_alert_dialog.view.*

class PSDialog private constructor() : BaseThemeDialogFragment() {

    companion object {
        private const val DIALOG_TAG = "PSDialog"

        private const val TITLE_EXTRA = "title"
        private const val DESC_EXTRA = "desc"
        private const val POSITIVE_BTN_EXTRA = "pos_btn_text"
        private const val NEGATIVE_BTN_EXTRA = "neg_btn_text"
        private const val CANCELABLE_EXTRA = "is_cancelable"
        private const val IS_OK_ONLY_EXTRA = "is_ok_only"

        fun showDialog(
            fragmentManager: FragmentManager,
            title: String?,
            description: String?,
            positiveButtonText: String?,
            positiveButtonClickListener: (() -> Unit)?,
            negativeButtonText: String?,
            negativeButtonClickListener: (() -> Unit)?,
            dismissDialogListener: (() -> Unit)?,
            isCancelable: Boolean?,
            isOkOnly: Boolean?): PSDialog {

            val dialogInstance = PSDialog()
            dialogInstance.arguments = Bundle().apply {
                title?.let {
                    putString(TITLE_EXTRA, it)
                }

                description?.let {
                    putString(DESC_EXTRA, it)
                }

                positiveButtonText?.let {
                    putString(POSITIVE_BTN_EXTRA, it)
                }

                negativeButtonText?.let {
                    putString(NEGATIVE_BTN_EXTRA, it)
                }

                putBoolean(CANCELABLE_EXTRA, isCancelable ?: DEFAULT_CANCELABLE)
                putBoolean(IS_OK_ONLY_EXTRA, isOkOnly ?: DEFAULT_IS_OK_ONLY)
            }

            dialogInstance.positiveClickListener = positiveButtonClickListener
            dialogInstance.negativeClickListener = negativeButtonClickListener
            dialogInstance.dismissDialogListener = dismissDialogListener

            return dialogInstance.also { it.show(fragmentManager, DIALOG_TAG) }
        }

        // Default values
        private val DEFAULT_TITLE_STRING_ID = R.string.app_name
        private val DEFAULT_POS_BTN_STRING_ID = R.string.ok_action_text
        private val DEFAULT_NEG_BTN_STRING_ID = R.string.cancel_action_text
        private const val DEFAULT_CANCELABLE = true
        private const val DEFAULT_IS_OK_ONLY = false
    }

    var positiveClickListener: (() -> Unit)? = null
    var negativeClickListener: (() -> Unit)? = null
    var dismissDialogListener: (() -> Unit)? = null

    private var containerView: View? = null
    private var titleTextView: TextView? = null
    private var descriptionTextView: TextView? = null
    private var positiveBtn: TextView? = null
    private var negativeBtn: TextView? = null

    override fun applyTheme(theme: CustomTheme) {
        super.applyTheme(theme)
        CustomThemeApplier.applyBackgroundTint(theme, containerView, R.attr.themedPopupBackgroundColor)
        CustomThemeApplier.applyTextColor(
            theme,
            R.attr.themedPrimaryTextColor,
            titleTextView,
            descriptionTextView,
            negativeBtn,
        )
        CustomThemeApplier.applyTextColor(theme, positiveBtn, R.attr.themedAccentColor)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dialogView = inflater.inflate(R.layout.layout_alert_dialog, container, false)
        isCancelable = arguments?.getBoolean(CANCELABLE_EXTRA, DEFAULT_CANCELABLE) ?: DEFAULT_CANCELABLE
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setWindowAnimations(R.style.popup_animation_medium)
        }
        return dialogView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dialogExtras = arguments
        if (dialogExtras == null) {
            dismissAllowingStateLoss()
            return
        }

        containerView = view.llAlertDialogContainer
        titleTextView = view.tvDialogTitle.apply {
            text = dialogExtras.getString(TITLE_EXTRA, getString(DEFAULT_TITLE_STRING_ID))
        }
        descriptionTextView = view.tvDialogDescription.apply {
            text = dialogExtras.getString(DESC_EXTRA)
        }
        positiveBtn = view.tvDialogPositiveButton.apply {
            text = dialogExtras.getString(POSITIVE_BTN_EXTRA, getString(DEFAULT_POS_BTN_STRING_ID))
            setClickListenerWithDefault(this, positiveClickListener) { dismissAllowingStateLoss() }
        }
        negativeBtn = view.tvDialogNegativeButton.apply {
            text = dialogExtras.getString(NEGATIVE_BTN_EXTRA, getString(DEFAULT_NEG_BTN_STRING_ID))
            setClickListenerWithDefault(this, negativeClickListener) { dismissAllowingStateLoss() }
            setVisibilityInvisible(!dialogExtras.getBoolean(IS_OK_ONLY_EXTRA, DEFAULT_IS_OK_ONLY))
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        dismissDialogListener?.invoke()
        super.onDismiss(dialog)
    }

    private inline fun setClickListenerWithDefault(
        view: View,
        noinline clickListener: (() -> Unit)?,
        crossinline defaultClickListener: () -> Unit
    ) {
        if (clickListener != null) {
            view.setOnClickListener { clickListener.invoke() }
        } else {
            view.setOnClickListener { defaultClickListener() }
        }
    }
}