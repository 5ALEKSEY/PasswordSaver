package com.ak.feature_tabaccounts_impl.screens.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.ak.base.extensions.setSafeClickListener
import com.ak.feature_tabaccounts_impl.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.layout_account_actions_dialog.view.*

class AccountActionsBottomSheetDialog : BottomSheetDialogFragment() {

    companion object {
        private const val BOTTOM_SHEET_DIALOG_TAG = "AccountActionsBottomSheetDialog"
        const val COPY_ACCOUNT_LOGIN_ACTION = 1
        const val COPY_ACCOUNT_PASSWORD_ACTION = 2
        const val EDIT_ACCOUNT_ITEM_ACTION = 3
        const val DELETE_ACCOUNT_ITEM_ACTION = 4

        fun showDialog(fragmentManager: FragmentManager): AccountActionsBottomSheetDialog {
            val sheetDialogInstance = AccountActionsBottomSheetDialog()
            sheetDialogInstance.show(
                fragmentManager,
                BOTTOM_SHEET_DIALOG_TAG
            )
            return sheetDialogInstance
        }
    }

    lateinit var onChooseAccountActionListener: (chooseActionId: Int) -> Unit

    private var mFragmentView: View? = null

    override fun getTheme() = R.style.PasswordActionsBottomSheetDialog

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mFragmentView = inflater.inflate(R.layout.layout_account_actions_dialog, container, false)
        return mFragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.tvCopyAccountLoginAction.setSafeClickListener {
            publishPasswordActionChoose(COPY_ACCOUNT_LOGIN_ACTION)
        }
        view.tvCopyAccountPasswordItemAction.setSafeClickListener {
            publishPasswordActionChoose(COPY_ACCOUNT_PASSWORD_ACTION)
        }
        view.tvEditAccountItemAction.setSafeClickListener {
            publishPasswordActionChoose(EDIT_ACCOUNT_ITEM_ACTION)
        }
        view.tvDeleteAccountItemAction.setSafeClickListener {
            publishPasswordActionChoose(DELETE_ACCOUNT_ITEM_ACTION)
        }
    }

    private fun publishPasswordActionChoose(passwordActionId: Int) {
        if (this::onChooseAccountActionListener.isInitialized) {
            onChooseAccountActionListener(passwordActionId)
        }
        dialog?.dismiss()
    }
}