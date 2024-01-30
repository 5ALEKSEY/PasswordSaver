package com.ak.feature_security_impl.auth.security.pincode

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.forEach
import androidx.core.view.isVisible
import com.ak.base.extensions.vibrate
import com.ak.feature_security_impl.R

class PincodeAuthView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private val cPincodeInputView by lazy { findViewById<PincodeContentInputView>(R.id.cPincodeInputView) }
    private val clRootPincodeAuthView by lazy { findViewById<ConstraintLayout>(R.id.clRootPincodeAuthView) }
    private val ivBackspaceAction by lazy { findViewById<ImageView>(R.id.ivBackspaceAction) }
    private val ivBiometricAuthMark by lazy { findViewById<ImageView>(R.id.ivBiometricAuthMark) }

    companion object {
        private const val FAILED_AUTH_VIBRATION_DELAY_IN_MILLIS = 300L
    }

    lateinit var onFinishedAction: (resultPincode: String) -> Unit

    private var isPincodeInputLocked = false

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.layout_pincode_auth_view, this, true)
        initViewListeners()
    }

    fun setBiometricAuthMarkVisibility(isVisible: Boolean) {
        ivBiometricAuthMark.visibility = if (isVisible) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    fun showBiometricMarkFailedAttempt() {
        if (ivBiometricAuthMark.isVisible) {
            val shakeAnimation = AnimationUtils.loadAnimation(context, R.anim.small_shake)
            ivBiometricAuthMark.startAnimation(shakeAnimation)
        }
    }

    fun setPincodeValuesCount(count: Int) {
        cPincodeInputView.setFillContentSize(count)
    }

    fun setFailedAuthViewState() {
        context.vibrate(FAILED_AUTH_VIBRATION_DELAY_IN_MILLIS)
    }

    fun setPincodeInputLockedState(isLocked: Boolean) {
        isPincodeInputLocked = isLocked
    }

    fun clearPincodeInput() {
        cPincodeInputView.clearContentInputView()
    }

    private fun initViewListeners() {
        clRootPincodeAuthView.forEach { childView ->
            if (childView is PincodeNumberView) {
                childView.setNumberClickListener(this::onPincodeNumberClicked)
            }
        }

        ivBackspaceAction.setOnClickListener { cPincodeInputView.removeLastPincodeValue() }
        cPincodeInputView.mOnContentFilled = { onFinishedAction.invoke(it) }
    }

    private fun onPincodeNumberClicked(pincodeValue: String) {
        if (!isPincodeInputLocked) {
            cPincodeInputView.putPincodeValue(pincodeValue)
        }
    }
}