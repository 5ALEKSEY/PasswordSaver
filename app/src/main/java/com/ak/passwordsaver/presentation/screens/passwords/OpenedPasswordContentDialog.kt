package com.ak.passwordsaver.presentation.screens.passwords

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog

class OpenedPasswordContentDialog : DialogFragment() {

    companion object {
        private const val DIALOG_TAG = "OpenedPasswordContentDialog"
        private const val TITLE_TEXT_KEY = "dialog_title_text"
        private const val DESCRIPTION_TEXT_KEY = "dialog_description_text"

        fun show(title: String, description: String, fragmentManager: FragmentManager): OpenedPasswordContentDialog {
            val dialogBundleData = Bundle().apply {
                this.putString(TITLE_TEXT_KEY, title)
                this.putString(DESCRIPTION_TEXT_KEY, description)
            }

            val dialogInstance = OpenedPasswordContentDialog()
            dialogInstance.arguments = dialogBundleData
            dialogInstance.show(fragmentManager, DIALOG_TAG)
            return dialogInstance
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireActivity()).apply {
            arguments?.also {
                this.setTitle(it.getString(TITLE_TEXT_KEY))
                this.setMessage(it.getString(DESCRIPTION_TEXT_KEY))
            }
            this.setPositiveButton("Ok") { dialog, _ ->
                dialog.dismiss()
            }
            this.setCancelable(false)
        }
        return dialogBuilder.create()
    }
}