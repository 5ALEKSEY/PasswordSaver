package com.ak.passwordsaver.presentation.screens.auth

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.base.ui.BasePSFragmentActivity
import com.ak.passwordsaver.presentation.screens.auth.security.patterncode.PatternAuthView
import com.ak.passwordsaver.presentation.screens.auth.security.pincode.PincodeAuthView
import com.ak.passwordsaver.utils.bindView
import com.ak.passwordsaver.utils.extensions.setSafeClickListener
import com.ak.passwordsaver.utils.extensions.vibrate
import com.arellomobile.mvp.presenter.InjectPresenter


class SecurityActivity : BasePSFragmentActivity(), ISecurityView {

    companion object {
        private const val IS_AUTH_ACTION_KEY_EXTRA = "is_auth_action"
        private const val PINCODE_INPUT_VALUES_COUNT = 4
        private const val SWITCH_AUTH_METHOD_DURATION = 200L
        private const val LOCK_VIBRATION_DELAY = 800L
        private const val CHANGE_AUTH_INPUT_METHOD_CLICK_DELAY = 400L

        fun startSecurityForResult(
            context: FragmentActivity,
            fragment: Fragment,
            securityAction: Int,
            requestCode: Int
        ) {
            val intent = getSecurityActivityIntent(context, securityAction)
            context.startActivityFromFragment(fragment, intent, requestCode)
        }

        fun startSecurityForResult(
            context: FragmentActivity,
            securityAction: Int,
            requestCode: Int
        ) {
            val intent = getSecurityActivityIntent(context, securityAction)
            context.startActivityForResult(intent, requestCode)
        }

        private fun getSecurityActivityIntent(
            context: FragmentActivity,
            securityAction: Int
        ) =
            Intent(context, SecurityActivity::class.java).apply {
                putExtra(IS_AUTH_ACTION_KEY_EXTRA, securityAction)
            }
    }

    @InjectPresenter
    lateinit var mSecurityPresenter: SecurityPresenter

    private val mPincodeAuthView: PincodeAuthView by bindView(R.id.v_pincode_auth_view)
    private val mPatternAuthView: PatternAuthView by bindView(R.id.v_pattern_auth_view)
    private val mSecurityMessageText: TextView by bindView(R.id.tv_security_message_text)
    private val mSecurityInputTypeImageView: ImageView by bindView(R.id.iv_security_input_type_change_action)

    override fun getScreenLayoutResId() = R.layout.activity_security

    override fun onBackPressed() {
        sendAuthActionResult(false)
    }

    override fun initViewBeforePresenterAttach() {
        super.initViewBeforePresenterAttach()
        intent?.let { initAuthState(it) }

        mPatternAuthView.mOnFinishedAction = mSecurityPresenter::onUserAuthFinished
        mPincodeAuthView.mOnFinishedAction = mSecurityPresenter::onUserAuthFinished
        mPincodeAuthView.setPincodeValuesCount(PINCODE_INPUT_VALUES_COUNT)

        mSecurityInputTypeImageView.setSafeClickListener(CHANGE_AUTH_INPUT_METHOD_CLICK_DELAY) {
            mSecurityPresenter.onSecurityInputTypeChangeClicked()
        }
    }

    override fun showSecurityMessage(message: String, withAnimation: Boolean) {
        mSecurityMessageText.visibility = View.VISIBLE
        mSecurityMessageText.text = message
        if (withAnimation) {
            val shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.small_shake)
            mSecurityMessageText.startAnimation(shakeAnimation)
        }
    }

    override fun hideSecurityMessage() {
        mSecurityMessageText.visibility = View.INVISIBLE
    }

    override fun showFailedPincodeAuthAction() {
        mPincodeAuthView.setFailedAuthViewState()
    }

    override fun showFailedPatternAuthAction() {
        mPatternAuthView.setFailedAuthViewState()
    }

    override fun lockSecurityInputViews() {
        vibrate(LOCK_VIBRATION_DELAY)
        mPincodeAuthView.setPincodeInputLockedState(true)
        mPatternAuthView.setAuthViewInputLockState(true)
    }

    override fun unlockSecurityInputViews() {
        mPincodeAuthView.setPincodeInputLockedState(false)
        mPatternAuthView.setAuthViewInputLockState(false)
    }

    override fun sendAuthActionResult(isSuccessfully: Boolean) {
        val result = if (isSuccessfully) Activity.RESULT_OK else Activity.RESULT_CANCELED
        setResult(result)
        finish()
    }

    override fun switchAuthMethod(isPincode: Boolean, withAnimation: Boolean) {
        val switchDuration = if (withAnimation) SWITCH_AUTH_METHOD_DURATION else 0L

        val appearView = if (isPincode) mPincodeAuthView else mPatternAuthView
        val disappearView = if (!isPincode) mPincodeAuthView else mPatternAuthView

        val appearAnim = getAppearAnimationForView(appearView, switchDuration)
        val disappearAnim = getDisappearAnimationForView(disappearView, switchDuration)
        disappearAnim.playSequentially(appearAnim)
        disappearAnim.start()

        appearView.visibility = View.VISIBLE
        Handler().postDelayed({ disappearView.visibility = View.GONE }, 2 * switchDuration)

        setSecurityInputTypeIcon(isPincode)
    }

    override fun lockSwitchAuthMethod() {
        mSecurityInputTypeImageView.visibility = View.GONE
    }

    override fun unlockSwitchAuthMethod() {
        mSecurityInputTypeImageView.visibility = View.VISIBLE
    }

    private fun initAuthState(intent: Intent) {
        mSecurityPresenter.mAuthActionType = intent.getIntExtra(
            IS_AUTH_ACTION_KEY_EXTRA,
            -1
        )
    }

    override fun isAuthCheckNeedsForScreen() = false

    private fun setSecurityInputTypeIcon(isPincode: Boolean) {
        (if (!isPincode) R.drawable.ic_pincode_input_type
        else R.drawable.ic_pattern_input_type).let(mSecurityInputTypeImageView::setImageResource)
    }

    private fun getAppearAnimationForView(view: View, duration: Long): AnimatorSet {
        val appearWithOffsetX = ObjectAnimator.ofFloat(view, View.SCALE_X, 0f, 1.1f)
        val appearWithOffsetY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 0f, 1.1f)
        val appearWithOffset = AnimatorSet()
        appearWithOffset.duration = duration
        appearWithOffset.playTogether(appearWithOffsetX, appearWithOffsetY)

        val appearToDefaultSizeX = ObjectAnimator.ofFloat(view, View.SCALE_X, 1f)
        val appearToDefaultSizeY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1f)
        val appearToDefaultSize = AnimatorSet()
        appearToDefaultSize.duration = duration / 3
        appearToDefaultSize.playTogether(appearToDefaultSizeX, appearToDefaultSizeY)


        return startAnimations(appearWithOffset, appearToDefaultSize)
    }

    private fun getDisappearAnimationForView(view: View, duration: Long): AnimatorSet {
        val disappearX = ObjectAnimator.ofFloat(view, View.SCALE_X, 1f, 0f)
        val disappearY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1f, 0f)
        val disappear = AnimatorSet()
        disappear.duration = duration / 2
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
