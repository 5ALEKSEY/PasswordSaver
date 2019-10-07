package com.ak.passwordsaver.presentation.screens.auth

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.base.constants.AppConstants
import com.ak.passwordsaver.presentation.base.ui.BasePSFragmentActivity
import com.ak.passwordsaver.presentation.screens.auth.security.patterncode.PatternAuthView
import com.ak.passwordsaver.utils.bindView
import com.arellomobile.mvp.presenter.InjectPresenter

class SecurityActivity : BasePSFragmentActivity(), ISecurityView {

    companion object {
        fun startSecurityForResult(context: FragmentActivity, fragment: Fragment) {
            val intent = Intent(context, SecurityActivity::class.java)
            context.startActivityFromFragment(fragment, intent, AppConstants.SECURITY_REQUEST_CODE)
        }

        fun startSecurityForResult(context: FragmentActivity) {
            val intent = Intent(context, SecurityActivity::class.java)
            context.startActivityForResult(intent, AppConstants.SECURITY_REQUEST_CODE)
        }
    }

    @InjectPresenter
    lateinit var mSecurityPresenter: SecurityPresenter

    private val mPatternAuthView: PatternAuthView by bindView(R.id.v_pattern_auth_view)
    private val mSecurityMessageText: TextView by bindView(R.id.tv_security_message_text)

    override fun getScreenLayoutResId() = R.layout.activity_security

    override fun onBackPressed() {
        finishActivityWithResult(true)
    }

    override fun initViewBeforePresenterAttach() {
        super.initViewBeforePresenterAttach()
        mPatternAuthView.mOnFinishedAction = mSecurityPresenter::onPatterAuthFinished
    }

    override fun showSecurityMessage(message: String, withAnimation: Boolean) {
        mSecurityMessageText.text = message
        if (withAnimation) {
            val shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.small_shake)
            mSecurityMessageText.startAnimation(shakeAnimation)
        }
    }

    override fun showSuccessPatternAuthAction() {
        mPatternAuthView.setAuthViewState(true)
    }

    override fun showFailedPatternAuthAction() {
        mPatternAuthView.setAuthViewState(false)
    }

    override fun lockPatternViewInput() {
        mPatternAuthView.setAuthViewInputLockState(true)
    }

    override fun unlockPatternViewInput() {
        mPatternAuthView.setAuthViewInputLockState(false)
    }

    override fun finishActivityWithResult(isCanceled: Boolean) {
        val result = if (isCanceled) Activity.RESULT_CANCELED else Activity.RESULT_OK
        setResult(result)
        finish()
    }
}
