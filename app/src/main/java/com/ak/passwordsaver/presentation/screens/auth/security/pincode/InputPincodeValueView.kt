package com.ak.passwordsaver.presentation.screens.auth.security.pincode

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.ak.passwordsaver.R
import com.ak.passwordsaver.utils.bindView

class InputPincodeValueView(context: Context?, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    companion object {
        private const val SHOW_HIDE_ANIMATION_DURATION = 150L
    }

    private val mInputPincodeValueTextView: TextView by bindView(R.id.tv_input_pincode_value_text)
    private val mInputPincodeSecretImageView: ImageView by bindView(R.id.iv_input_pincode_secret_image)
    private var mIsSecretPincodeState = false

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.layout_input_pincode_value_view, this, true)
    }

    fun showInputPincodeValue(value: String) {
        mIsSecretPincodeState = false
        mInputPincodeSecretImageView.visibility = View.INVISIBLE
        mInputPincodeValueTextView.text = value
        mInputPincodeValueTextView.visibility = View.VISIBLE
        getAppearAnimationForView(mInputPincodeValueTextView).start()
    }

    fun setSecretStateForPincodeValue() {
        if (!mIsSecretPincodeState) {
            getDisappearAnimationForView(mInputPincodeValueTextView).start()
            postDelayed(
                {
                    mInputPincodeValueTextView.visibility = View.INVISIBLE
                    mInputPincodeSecretImageView.visibility = View.VISIBLE
                },
                SHOW_HIDE_ANIMATION_DURATION
            )
            mIsSecretPincodeState = true
        }
    }

    private fun getAppearAnimationForView(view: View): AnimatorSet {
        val appearX = ObjectAnimator.ofFloat(view, View.SCALE_X, 0f, 1f)
        val appearY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 0f, 1f)
        val appear = AnimatorSet()
        appear.duration = SHOW_HIDE_ANIMATION_DURATION
        appear.playTogether(appearX, appearY)

        return startAnimations(appear)
    }

    private fun getDisappearAnimationForView(view: View): AnimatorSet {
        val disappearX = ObjectAnimator.ofFloat(view, View.SCALE_X, 1f, 0f)
        val disappearY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1f, 0f)
        val disappear = AnimatorSet()
        disappear.duration = SHOW_HIDE_ANIMATION_DURATION
        disappear.playTogether(disappearX, disappearY)

        return startAnimations(disappear)
    }

    private fun startAnimations(vararg animations: Animator): AnimatorSet {
        val animSet = AnimatorSet()
        animSet.playSequentially(*animations)
        animSet.interpolator = AccelerateDecelerateInterpolator()
        return animSet
    }
}