package com.ak.feature_tabpasswords_impl.screens.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import com.ak.base.extensions.setSafeClickListener
import com.ak.feature_tabpasswords_impl.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.layout_password_actions_dialog.view.llCopyPasswordContentAction
import kotlinx.android.synthetic.main.layout_password_actions_dialog.view.llDeletePasswordItemAction
import kotlinx.android.synthetic.main.layout_password_actions_dialog.view.llEditPasswordItemAction
import kotlinx.android.synthetic.main.layout_password_actions_dialog.view.llPasswordContentVisibilityAction
import kotlinx.android.synthetic.main.layout_password_actions_dialog.view.tvPasswordContentVisibility

class PasswordActionsBottomSheetDialog : BottomSheetDialogFragment() {

    companion object {
        private const val BOTTOM_SHEET_DIALOG_TAG = "PasswordActionsBottomSheetDialog"
        private const val IS_PASS_CONTENT_VISIBLE_EXTRA_KEY = "IS_PASS_CONTENT_VISIBLE"

        const val SHOW_PASSWORD_CONTENT_ACTION = 1
        const val HIDE_PASSWORD_CONTENT_ACTION = 2
        const val COPY_PASSWORD_CONTENT_ACTION = 3
        const val EDIT_PASSWORD_ITEM_ACTION = 4
        const val DELETE_PASSWORD_ITEM_ACTION = 5

        fun showDialog(
            fragmentManager: FragmentManager,
            isPasswordContentVisible: Boolean
        ): PasswordActionsBottomSheetDialog {
            val sheetDialogInstance = PasswordActionsBottomSheetDialog().apply {
                arguments = bundleOf(IS_PASS_CONTENT_VISIBLE_EXTRA_KEY to isPasswordContentVisible)
            }
            sheetDialogInstance.show(
                fragmentManager,
                BOTTOM_SHEET_DIALOG_TAG
            )
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

        check(arguments != null) {
            "$BOTTOM_SHEET_DIALOG_TAG's arguments is null"
        }
        val isPasswordContentVisible = arguments?.getBoolean(
            IS_PASS_CONTENT_VISIBLE_EXTRA_KEY,
            false
        ) ?: false

        val textResId = if (isPasswordContentVisible) {
            R.string.chooser_hide_password_content
        } else {
            R.string.chooser_show_password_content
        }
        view.tvPasswordContentVisibility.text = getString(textResId)
        view.llPasswordContentVisibilityAction.setSafeClickListener {
            val action = if (isPasswordContentVisible) {
                HIDE_PASSWORD_CONTENT_ACTION
            } else {
                SHOW_PASSWORD_CONTENT_ACTION
            }
            publishPasswordActionChoose(action)
        }

        view.llCopyPasswordContentAction.setSafeClickListener {
            publishPasswordActionChoose(COPY_PASSWORD_CONTENT_ACTION)
        }

        view.llEditPasswordItemAction.setSafeClickListener {
            publishPasswordActionChoose(EDIT_PASSWORD_ITEM_ACTION)
        }

        view.llDeletePasswordItemAction.setSafeClickListener {
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