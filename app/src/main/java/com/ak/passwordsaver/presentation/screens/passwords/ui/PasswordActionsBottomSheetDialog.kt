package com.ak.passwordsaver.presentation.screens.passwords.ui

import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ak.passwordsaver.R
import com.ak.passwordsaver.utils.extensions.setSafeClickListener

class PasswordActionsBottomSheetDialog : BottomSheetDialogFragment() {

    companion object {
        private const val BOTTOM_SHEET_DIALOG_TAG = "PasswordActionsBottomSheetDialog"
        const val COPY_PASSWORD_CONTENT_ACTION = 1
        const val EDIT_PASSWORD_ITEM_ACTION = 2
        const val DELETE_PASSWORD_ITEM_ACTION = 3

        fun showDialog(fragmentManager: FragmentManager): PasswordActionsBottomSheetDialog {
            val sheetDialogInstance = PasswordActionsBottomSheetDialog()
            sheetDialogInstance.show(fragmentManager, BOTTOM_SHEET_DIALOG_TAG)
            return sheetDialogInstance
        }
    }

    lateinit var onChoosePasswordActionListener: (chooseActionId: Int) -> Unit

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.tv_copy_password_content_action).setSafeClickListener {
            publishPasswordActionChoose(COPY_PASSWORD_CONTENT_ACTION)
        }
        view.findViewById<View>(R.id.tv_edit_password_item_action).setSafeClickListener {
            publishPasswordActionChoose(EDIT_PASSWORD_ITEM_ACTION)
        }
        view.findViewById<View>(R.id.tv_delete_password_item_action).setSafeClickListener {
            publishPasswordActionChoose(DELETE_PASSWORD_ITEM_ACTION)
        }
    }

    private fun publishPasswordActionChoose(passwordActionId: Int) {
        if (this::onChoosePasswordActionListener.isInitialized) {
            onChoosePasswordActionListener(passwordActionId)
        }
        dialog?.dismiss()
    }
}