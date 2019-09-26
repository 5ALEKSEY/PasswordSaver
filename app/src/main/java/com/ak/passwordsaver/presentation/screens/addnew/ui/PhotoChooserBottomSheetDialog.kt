package com.ak.passwordsaver.presentation.screens.addnew.ui

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ak.passwordsaver.R

class PhotoChooserBottomSheetDialog : BottomSheetDialogFragment() {

    companion object {
        private const val BOTTOM_SHEET_DIALOG_TAG = "PhotoChooserBottomSheetDialog"

        fun show(fragmentManager: FragmentManager): PhotoChooserBottomSheetDialog {
            val sheetDialogInstance = PhotoChooserBottomSheetDialog()
            sheetDialogInstance.show(fragmentManager, BOTTOM_SHEET_DIALOG_TAG)
            return sheetDialogInstance
        }
    }

    private var fragmentView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentView = inflater.inflate(R.layout.layout_photo_chooser_button, container, false)
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {

    }
}