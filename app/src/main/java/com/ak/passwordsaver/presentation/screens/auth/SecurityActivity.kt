package com.ak.passwordsaver.presentation.screens.auth

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.base.ui.BasePSFragmentActivity
import com.ak.passwordsaver.utils.extensions.setSafeClickListener
import com.ak.passwordsaver.utils.extensions.vibrate
import kotlinx.android.synthetic.main.activity_security.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class SecurityActivity : BasePSFragmentActivity<SecurityPresenter>(), ISecurityView {

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
    lateinit var securityPresenter: SecurityPresenter

    @ProvidePresenter
    fun providePresenter(): SecurityPresenter = daggerPresenter

    override fun getScreenLayoutResId() = R.layout.activity_security

    override fun onBackPressed() {
        sendAuthActionResult(false)
    }

    override fun initViewBeforePresenterAttach() {
        super.initViewBeforePresenterAttach()
        intent?.let { initAuthState(it) }

        vPatternAuthView.mOnFinishedAction = securityPresenter::onUserAuthFinished
        cPincodeAuthView.mOnFinishedAction = securityPresenter::onUserAuthFinished
        cPincodeAuthView.setPincodeValuesCount(PINCODE_INPUT_VALUES_COUNT)

        ivSecurityInputTypeChangeAction.setSafeClickListener(CHANGE_AUTH_INPUT_METHOD_CLICK_DELAY) {
            securityPresenter.onSecurityInputTypeChangeClicked()
        }
    }

    override fun showSecurityMessage(message: String, withAnimation: Boolean) {
        tvSecurityMessageText.visibility = View.VISIBLE
        tvSecurityMessageText.text = message
        if (withAnimation) {
            val shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.small_shake)
            tvSecurityMessageText.startAnimation(shakeAnimation)
        }
    }

    override fun hideSecurityMessage() {
        tvSecurityMessageText.visibility = View.INVISIBLE
    }

    override fun showFailedPincodeAuthAction() {
        cPincodeAuthView.setFailedAuthViewState()
    }

    override fun showFailedPatternAuthAction() {
        vPatternAuthView.setFailedAuthViewState()
    }

    override fun lockSecurityInputViews() {
        vibrate(LOCK_VIBRATION_DELAY)
        cPincodeAuthView.setPincodeInputLockedState(true)
        vPatternAuthView.setAuthViewInputLockState(true)
    }

    override fun unlockSecurityInputViews() {
        cPincodeAuthView.setPincodeInputLockedState(false)
        vPatternAuthView.setAuthViewInputLockState(false)
    }

    override fun sendAuthActionResult(isSuccessfully: Boolean) {
        val result = if (isSuccessfully) Activity.RESULT_OK else Activity.RESULT_CANCELED
        setResult(result)
        finish()
    }

    override fun switchAuthMethod(isPincode: Boolean, withAnimation: Boolean) {
        val switchDuration = if (withAnimation) SWITCH_AUTH_METHOD_DURATION else 0L

        val appearView = if (isPincode) cPincodeAuthView else vPatternAuthView
        val disappearView = if (!isPincode) cPincodeAuthView else vPatternAuthView

        val appearAnim = getAppearAnimationForView(appearView, switchDuration)
        val disappearAnim = getDisappearAnimationForView(disappearView, switchDuration)
        disappearAnim.playSequentially(appearAnim)
        disappearAnim.start()

        appearView.visibility = View.VISIBLE
        Handler().postDelayed({ disappearView.visibility = View.GONE }, 2 * switchDuration)

        setSecurityInputTypeIcon(isPincode)
    }

    override fun lockSwitchAuthMethod() {
        ivSecurityInputTypeChangeAction.visibility = View.GONE
    }

    override fun unlockSwitchAuthMethod() {
        ivSecurityInputTypeChangeAction.visibility = View.VISIBLE
    }

    private fun initAuthState(intent: Intent) {
        securityPresenter.mAuthActionType = intent.getIntExtra(
            IS_AUTH_ACTION_KEY_EXTRA,
            -1
        )
    }

    override fun isAuthCheckNeedsForScreen() = false

    private fun setSecurityInputTypeIcon(isPincode: Boolean) {
        (if (!isPincode) R.drawable.ic_pincode_input_type
        else R.drawable.ic_pattern_input_type).let(ivSecurityInputTypeChangeAction::setImageResource)
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
