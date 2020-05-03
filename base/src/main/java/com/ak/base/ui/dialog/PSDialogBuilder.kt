package com.ak.base.ui.dialog

import android.view.View
import androidx.fragment.app.FragmentManager

class PSDialogBuilder constructor(private val fragmentManager: FragmentManager) {
    private var title: String? = null
    private var desc: String? = null
    private var posBtnText: String? = null
    private var posClickListener: View.OnClickListener? = null
    private var negBtnText: String? = null
    private var negClickListener: View.OnClickListener? = null
    private var isCancelable: Boolean? = null
    private var isOkOnly: Boolean? = null

    fun title(titleText: String) = this.apply { title = titleText }
    fun description(descriptionText: String) = this.apply { desc = descriptionText }
    fun positiveButtonText(positiveButtonText: String) = this.apply { posBtnText = positiveButtonText }
    fun positive(positiveButtonText: String, positiveClickListener: View.OnClickListener) = this.apply {
        posBtnText = positiveButtonText
        posClickListener = positiveClickListener
    }

    fun positiveClickListener(positiveClickListener: View.OnClickListener) = this.apply {
        posClickListener = positiveClickListener
    }

    fun negativeButtonText(negativeButtonText: String) = this.apply { negBtnText = negativeButtonText }
    fun negative(negativeButtonText: String, negativeClickListener: View.OnClickListener) = this.apply {
        negBtnText = negativeButtonText
        negClickListener = negativeClickListener
    }

    fun negativeClickListener(negativeClickListener: View.OnClickListener) = this.apply {
        negClickListener = negativeClickListener
    }

    fun cancelable(isCancelable: Boolean) = this.apply { this.isCancelable = isCancelable }
    fun onlyOk(isOkOnly: Boolean) = this.apply { this.isOkOnly = isOkOnly }

    fun build() = PSDialog.showDialog(
            fragmentManager,
            title,
            desc,
            posBtnText,
            posClickListener,
            negBtnText,
            negClickListener,
            isCancelable,
            isOkOnly
    )
}