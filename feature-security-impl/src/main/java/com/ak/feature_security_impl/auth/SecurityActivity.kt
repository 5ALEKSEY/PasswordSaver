package com.ak.feature_security_impl.auth

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.uicomponents.BaseThemeActivity
import com.ak.base.extensions.setSafeClickListener
import com.ak.base.extensions.vibrate
import com.ak.base.viewmodel.injectViewModel
import com.ak.feature_security_api.interfaces.IAuthCheckerStarter
import com.ak.feature_security_impl.R
import com.ak.feature_security_impl.di.FeatureSecurityComponent
import com.ak.feature_security_impl.di.modules.SecurityViewModelsModule
import javax.inject.Inject
import javax.inject.Named
import kotlinx.android.synthetic.main.activity_security.ivSecurityInputTypeChangeAction
import kotlinx.android.synthetic.main.activity_security.tvSecurityMessageText
import kotlinx.android.synthetic.main.activity_security.vPatternAuthView
import kotlinx.android.synthetic.main.activity_security.vPincodeAuthView

class SecurityActivity : BaseThemeActivity() {

    companion object {
        private const val IS_AUTH_ACTION_KEY_EXTRA = "is_auth_action"
        private const val PINCODE_INPUT_VALUES_COUNT = 4
        private const val SWITCH_AUTH_METHOD_DURATION = 200L
        private const val LOCK_VIBRATION_DELAY = 800L
        private const val CHANGE_AUTH_INPUT_METHOD_CLICK_DELAY = 400L

        fun startSecurityForResult(context: FragmentActivity, fragment: Fragment, securityAction: Int, requestCode: Int) {
            val intent = getSecurityActivityIntent(context, securityAction)
            context.startActivityFromFragment(fragment, intent, requestCode)
            applyStartSecurityAnim(context)
        }

        fun startSecurityForResult(context: FragmentActivity, securityAction: Int, requestCode: Int) {
            val intent = getSecurityActivityIntent(context, securityAction)
            context.startActivityForResult(intent, requestCode)
            applyStartSecurityAnim(context)
        }

        private fun getSecurityActivityIntent(context: FragmentActivity, securityAction: Int) =
            Intent(context, SecurityActivity::class.java).apply {
                putExtra(IS_AUTH_ACTION_KEY_EXTRA, securityAction)
            }

        private fun applyStartSecurityAnim(context: FragmentActivity) {
            context.overridePendingTransition(R.anim.security_fade_in_animation, R.anim.security_fade_out_animation)
        }

        private fun applyFinishSecurityAnim(context: FragmentActivity) {
            context.overridePendingTransition(R.anim.security_fade_in_animation, R.anim.security_fade_out_animation)
        }
    }

    @Inject
    @field:Named(SecurityViewModelsModule.SECURITY_VIEW_MODELS_FACTORY_KEY)
    protected lateinit var viewModelsFactory: ViewModelProvider.Factory

    private lateinit var viewModel: SecurityViewModel

    override fun onBackPressed() {
        sendAuthActionResult(false)
    }

    override fun getNavigationBarColorResource(): Int {
        return R.attr.themedPrimaryColor
    }

    override fun isAppearanceLightNavigationBars(theme: CustomTheme): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        FeatureSecurityComponent.get().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_security)
        initViewModel()
        initView()
        subscribeToViewModel(viewModel)
    }

    private fun initViewModel() {
        viewModel = injectViewModel(viewModelsFactory)
        intent?.let {
            viewModel.authActionType = it.getIntExtra(
                IS_AUTH_ACTION_KEY_EXTRA,
                IAuthCheckerStarter.AUTH_SECURITY_ACTION_TYPE
            )
        }
        viewModel.initSecurityAuthState()
    }

    private fun subscribeToViewModel(viewModel: SecurityViewModel) {
        viewModel.subscribeToSwitchAuthMethodLiveData().observe(this) {
            switchAuthMethod(it.first, it.second)
        }
        viewModel.subscribeToShowSecurityMessageLiveData().observe(this) {
            showSecurityMessage(it.first, it.second)
        }
        viewModel.subscribeToHideSecurityMessageLiveData().observe(this) {
            hideSecurityMessage()
        }
        viewModel.subscribeToFailedPincodeAuthActionLiveData().observe(this) {
            showFailedPincodeAuthAction()
        }
        viewModel.subscribeToFailedPatternAuthActionLiveData().observe(this) {
            showFailedPatternAuthAction()
        }
        viewModel.subscribeToSecurityInputLockStateLiveData().observe(this) { isLocked ->
            if (isLocked) lockSecurityInputViews() else unlockSecurityInputViews()
        }
        viewModel.subscribeToSwitchAuthMethodLockStateLiveData().observe(this) { isLocked ->
            if (isLocked) lockSwitchAuthMethod() else unlockSwitchAuthMethod()
        }
        viewModel.subscribeToBiometricAuthVisibilityStateLiveData().observe(this, this::setBiometricAuthVisibility)
        viewModel.subscribeToBiometricAuthLockStateLiveData().observe(this, this::setBiometricAuthLockState)
        viewModel.subscribeToBiometricAuthFailedAttemptLiveData().observe(this) {
            showBiometricFailedAttempt()
        }
        viewModel.subscribeToAuthActionResultLiveData().observe(this, this::sendAuthActionResult)
    }

    override fun onResume() {
        super.onResume()
        viewModel.startBiometricAuth()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopBiometricAuth()
    }

    private fun showSecurityMessage(message: String, withAnimation: Boolean) {
        tvSecurityMessageText.visibility = View.VISIBLE
        tvSecurityMessageText.text = message
        if (withAnimation) {
            val shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.small_shake)
            tvSecurityMessageText.startAnimation(shakeAnimation)
        }
    }

    private fun hideSecurityMessage() {
        tvSecurityMessageText.visibility = View.INVISIBLE
    }

    private fun showFailedPincodeAuthAction() {
        vPincodeAuthView.setFailedAuthViewState()
    }

    private fun showFailedPatternAuthAction() {
        vPatternAuthView.setFailedAuthViewState()
    }

    private fun lockSecurityInputViews() {
        vibrate(LOCK_VIBRATION_DELAY)
        vPincodeAuthView.setPincodeInputLockedState(true)
        vPatternAuthView.setAuthViewInputLockState(true)
    }

    private fun unlockSecurityInputViews() {
        vPincodeAuthView.setPincodeInputLockedState(false)
        vPatternAuthView.setAuthViewInputLockState(false)
        viewModel.startBiometricAuth()
    }

    private fun setBiometricAuthVisibility(isBiometricVisible: Boolean) {
        vPincodeAuthView.setBiometricAuthMarkVisibility(isBiometricVisible)
    }

    private fun showBiometricFailedAttempt() {
        vPincodeAuthView.showBiometricMarkFailedAttempt()
    }

    private fun setBiometricAuthLockState(isLocked: Boolean) {
        
    }

    private fun sendAuthActionResult(isSuccessfully: Boolean) {
        val result = if (isSuccessfully) Activity.RESULT_OK else Activity.RESULT_CANCELED
        setResult(result)
        finish()
        applyFinishSecurityAnim(this)
    }

    private fun switchAuthMethod(isPincode: Boolean, withAnimation: Boolean) {
        val switchDuration = if (withAnimation) SWITCH_AUTH_METHOD_DURATION else 0L

        val appearView = if (isPincode) vPincodeAuthView else vPatternAuthView
        val disappearView = if (!isPincode) vPincodeAuthView else vPatternAuthView

        val appearAnim = getAppearAnimationForView(appearView, switchDuration)
        val disappearAnim = getDisappearAnimationForView(disappearView, switchDuration)
        disappearAnim.playSequentially(appearAnim)
        disappearAnim.start()

        appearView.visibility = View.VISIBLE
        Handler().postDelayed({ disappearView.visibility = View.GONE }, 2 * switchDuration)

        setSecurityInputTypeIcon(isPincode)
    }

    private fun lockSwitchAuthMethod() {
        ivSecurityInputTypeChangeAction.visibility = View.GONE
    }

    private fun unlockSwitchAuthMethod() {
        ivSecurityInputTypeChangeAction.visibility = View.VISIBLE
    }

    private fun initView() {
        vPatternAuthView.mOnFinishedAction = viewModel::onUserAuthFinished
        vPincodeAuthView.onFinishedAction = viewModel::onUserAuthFinished
        vPincodeAuthView.setPincodeValuesCount(PINCODE_INPUT_VALUES_COUNT)

        ivSecurityInputTypeChangeAction.setSafeClickListener(CHANGE_AUTH_INPUT_METHOD_CLICK_DELAY) {
            viewModel.onSecurityInputTypeChangeClicked()
        }
    }

    private fun setSecurityInputTypeIcon(isPincode: Boolean) {
        ivSecurityInputTypeChangeAction.setImageResource(
            if (!isPincode) R.drawable.ic_pincode_input_type
            else R.drawable.ic_pattern_input_type
        )
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
