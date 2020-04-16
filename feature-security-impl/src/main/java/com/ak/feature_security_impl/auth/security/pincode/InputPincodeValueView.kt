package com.ak.feature_security_impl.auth.security.pincode

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import com.ak.feature_security_impl.R
import kotlinx.android.synthetic.main.layout_input_pincode_value_view.view.*

class InputPincodeValueView(context: Context?, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    companion object {
        private const val SHOW_HIDE_ANIMATION_DURATION = 150L
    }

    private var mIsSecretPincodeState = false

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.layout_input_pincode_value_view, this, true)
    }

    fun showInputPincodeValue(value: String) {
        mIsSecretPincodeState = false
        ivInputPincodeSecretImage.visibility = View.INVISIBLE
        tvInputPincodeValueText.text = value
        tvInputPincodeValueText.visibility = View.VISIBLE
        getAppearAnimationForView(tvInputPincodeValueText).start()
    }

    fun setSecretStateForPincodeValue() {
        if (!mIsSecretPincodeState) {
            getDisappearAnimationForView(tvInputPincodeValueText).start()
            postDelayed(
                {
                    tvInputPincodeValueText.visibility = View.INVISIBLE
                    ivInputPincodeSecretImage.visibility = View.VISIBLE
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
        appear.duration =
            SHOW_HIDE_ANIMATION_DURATION
        appear.playTogether(appearX, appearY)

        return startAnimations(appear)
    }

    private fun getDisappearAnimationForView(view: View): AnimatorSet {
        val disappearX = ObjectAnimator.ofFloat(view, View.SCALE_X, 1f, 0f)
        val disappearY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1f, 0f)
        val disappear = AnimatorSet()
        disappear.duration =
            SHOW_HIDE_ANIMATION_DURATION
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