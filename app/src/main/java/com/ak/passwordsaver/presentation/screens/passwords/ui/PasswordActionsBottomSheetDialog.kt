package com.ak.passwordsaver.presentation.screens.passwords.ui

import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ak.passwordsaver.R

class PasswordActionsBottomSheetDialog : BottomSheetDialogFragment() {

    companion object {
        private const val BOTTOM_SHEET_DIALOG_TAG = "PasswordActionsBottomSheetDialog"

        fun show(fragmentManager: FragmentManager): PasswordActionsBottomSheetDialog {
            val sheetDialogInstance = PasswordActionsBottomSheetDialog()
            sheetDialogInstance.show(fragmentManager, BOTTOM_SHEET_DIALOG_TAG)
            return sheetDialogInstance
        }
    }

    private var mFragmentView: View? = null

    override fun getTheme() = R.style.PasswordActionsBottomSheetDialog

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mFragmentView = inflater.inflate(R.layout.layout_password_actions_dialog, container, false)
        return mFragmentView
    }
}