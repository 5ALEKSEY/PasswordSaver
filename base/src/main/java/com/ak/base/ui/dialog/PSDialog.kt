package com.ak.base.ui.dialog

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.ak.base.R
import kotlinx.android.synthetic.main.layout_alert_dialog.view.*

class PSDialog private constructor() : DialogFragment() {

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
        private const val DEFAULT_TITLE = "Password Saver"
        private const val DEFAULT_POS_BTN_TEXT = "Ok"
        private const val DEFAULT_NEG_BTN_TEXT = "Cancel"
        private const val DEFAULT_CANCELABLE = true
        private const val DEFAULT_IS_OK_ONLY = false
    }

    var positiveClickListener: (() -> Unit)? = null
    var negativeClickListener: (() -> Unit)? = null
    var dismissDialogListener: (() -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dialogView = inflater.inflate(R.layout.layout_alert_dialog, container, false)
        isCancelable = arguments?.getBoolean(CANCELABLE_EXTRA, DEFAULT_CANCELABLE) ?: DEFAULT_CANCELABLE
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setWindowAnimations(R.style.ps_dialog_animation)
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

        view.tvDialogTitle.text = dialogExtras.getString(TITLE_EXTRA, DEFAULT_TITLE)
        view.tvDialogDescription.text = dialogExtras.getString(DESC_EXTRA)

        view.tvDialogPositiveButton.text = dialogExtras.getString(POSITIVE_BTN_EXTRA, DEFAULT_POS_BTN_TEXT)
        setClickListenerWithDefault(view.tvDialogPositiveButton, positiveClickListener) { dismissAllowingStateLoss() }

        view.tvDialogNegativeButton.text = dialogExtras.getString(NEGATIVE_BTN_EXTRA, DEFAULT_NEG_BTN_TEXT)
        setClickListenerWithDefault(view.tvDialogNegativeButton, negativeClickListener) { dismissAllowingStateLoss() }

        view.tvDialogNegativeButton.visibility = if (dialogExtras.getBoolean(IS_OK_ONLY_EXTRA, DEFAULT_IS_OK_ONLY)) {
            View.INVISIBLE
        } else {
            View.VISIBLE
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