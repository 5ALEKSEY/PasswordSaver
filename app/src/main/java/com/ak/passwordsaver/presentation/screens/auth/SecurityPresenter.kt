package com.ak.passwordsaver.presentation.screens.auth

import android.util.Log
import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.data.model.preferences.settings.ISettingsPreferencesManager
import com.ak.passwordsaver.presentation.base.BasePSPresenter
import com.ak.passwordsaver.presentation.base.constants.AppConstants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@InjectViewState
class SecurityPresenter : BasePSPresenter<ISecurityView>() {

    companion object {
        const val AUTH_SECURITY_ACTION_TYPE = 1
        const val ADD_PINCODE_SECURITY_ACTION_TYPE = 2
        const val CHANGE_PINCODE_SECURITY_ACTION_TYPE = 3
        const val ADD_PATTERN_SECURITY_ACTION_TYPE = 4
        const val CHANGE_PATTERN_SECURITY_ACTION_TYPE = 5

        private const val MAX_FAILED_ATTEMPTS_COUNT = 5
        private const val MIN_CODE_LENGTH = 5
    }

    @Inject
    lateinit var mSettingsPreferencesManager: ISettingsPreferencesManager

    var mAuthActionType = AUTH_SECURITY_ACTION_TYPE
    private var mIsPincodeAuthMethod = true

    private var mFailedAttemptsCount = 0
    private var mAddSecurityConfirmCode = ""

    init {
        PSApplication.appInstance.getApplicationComponent().inject(this)
    }

    fun onSecurityInputTypeChangeClicked() {
        mIsPincodeAuthMethod = !mIsPincodeAuthMethod
        viewState.switchAuthMethod(mIsPincodeAuthMethod, true)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        initSecurityAuthState()
    }

    private fun initSecurityAuthState() {
        viewState.lockSwitchAuthMethod()
        when (mAuthActionType) {
            AUTH_SECURITY_ACTION_TYPE -> {
                viewState.switchAuthMethod(mIsPincodeAuthMethod)
                if (mSettingsPreferencesManager.isPatternEnabled()) {
                    viewState.unlockSwitchAuthMethod()
                }
                val blockSecurityInputTime = mSettingsPreferencesManager.getBlockSecurityInputTime()
                val leftBlockInterval = (System.currentTimeMillis() - blockSecurityInputTime) / 1000
                if (leftBlockInterval > AppConstants.BLOCK_SECURITY_INPUT_DELAY) {
                    resetAttemptsAndUnlockSecurityInput()
                } else {
                    viewState.lockSecurityInputViews()
                    val timeForBlock = AppConstants.BLOCK_SECURITY_INPUT_DELAY - leftBlockInterval
                    startBlockSecurityInputTimer(timeForBlock)
                }
            }
            ADD_PINCODE_SECURITY_ACTION_TYPE -> {
                viewState.showSecurityMessage("Enter your personal pincode")
                viewState.switchAuthMethod(true)
            }
            ADD_PATTERN_SECURITY_ACTION_TYPE -> {
                viewState.showSecurityMessage("Create your personal graph pattern")
                mIsPincodeAuthMethod = false
                viewState.switchAuthMethod(false)
            }
            CHANGE_PINCODE_SECURITY_ACTION_TYPE -> {
                viewState.showSecurityMessage("Enter your previous pincode")
                viewState.switchAuthMethod(true)
            }
            CHANGE_PATTERN_SECURITY_ACTION_TYPE -> {
                viewState.showSecurityMessage("Enter your previous graph pattern")
                mIsPincodeAuthMethod = false
                viewState.switchAuthMethod(false)
            }
        }
    }

    fun onUserAuthFinished(inputCode: String) {
        when {
            mAuthActionType == AUTH_SECURITY_ACTION_TYPE -> handleAuthUserInput(inputCode)
            isAddNewSecurityAuthActionType() -> handleAddNewSecurityUserInput(inputCode)
            isChangeSecurityAuthActionType() -> handleChangeSecurityUserInput(inputCode)
        }
    }

    private fun isAddNewSecurityAuthActionType() =
        mAuthActionType == ADD_PINCODE_SECURITY_ACTION_TYPE
                || mAuthActionType == ADD_PATTERN_SECURITY_ACTION_TYPE

    private fun isChangeSecurityAuthActionType() =
        mAuthActionType == CHANGE_PINCODE_SECURITY_ACTION_TYPE
                || mAuthActionType == CHANGE_PATTERN_SECURITY_ACTION_TYPE

    private fun handleChangeSecurityUserInput(inputCode: String) {
        if (isSuccessfulUserInputCode(inputCode)) {
            mAuthActionType = when (mAuthActionType) {
                CHANGE_PINCODE_SECURITY_ACTION_TYPE -> ADD_PINCODE_SECURITY_ACTION_TYPE
                CHANGE_PATTERN_SECURITY_ACTION_TYPE -> ADD_PATTERN_SECURITY_ACTION_TYPE
                else -> ADD_PINCODE_SECURITY_ACTION_TYPE
            }
            initSecurityAuthState()
        } else {
            viewState.showSecurityMessage("Nope, not that :)", true)
            showFailedActionViewState()
        }
    }

    private fun handleAddNewSecurityUserInput(inputCode: String) {
        when {
            mAddSecurityConfirmCode.isNotEmpty() -> {
                if (mAddSecurityConfirmCode.equals(inputCode, true)) {
                    viewState.hideSecurityMessage()
                    when (mAuthActionType) {
                        ADD_PINCODE_SECURITY_ACTION_TYPE -> {
                            mSettingsPreferencesManager.setPincodeEnableState(true)
                            mSettingsPreferencesManager.setUserPincodeValue(mAddSecurityConfirmCode)
                        }
                        ADD_PATTERN_SECURITY_ACTION_TYPE -> {
                            mSettingsPreferencesManager.setPatternEnableState(true)
                            mSettingsPreferencesManager.setUserPatternValue(mAddSecurityConfirmCode)
                        }
                    }
                    viewState.sendAuthActionResult(true)
                } else {
                    viewState.showSecurityMessage(getMessageForConfirmCode(), true)
                    showFailedActionViewState()
                }
            }
            inputCode.length < MIN_CODE_LENGTH && mAuthActionType == ADD_PATTERN_SECURITY_ACTION_TYPE -> {
                viewState.showSecurityMessage(
                    "Why is it so simple? Please, add more lines :)",
                    true
                )
                showFailedActionViewState()
            }
            mAddSecurityConfirmCode.isEmpty() -> {
                viewState.showSecurityMessage(getMessageForConfirmCode())
                mAddSecurityConfirmCode = inputCode
            }
        }
    }

    private fun getMessageForConfirmCode() =
        if (mIsPincodeAuthMethod) {
            "Enter the pincode again to confirm"
        } else {
            "Enter the graph pattern again to confirm"
        }

    private fun handleAuthUserInput(inputCode: String) {
        when {
            isSuccessfulUserInputCode(inputCode) -> {
                viewState.hideSecurityMessage()
                viewState.sendAuthActionResult(true)
            }
            mFailedAttemptsCount != MAX_FAILED_ATTEMPTS_COUNT -> {
                mFailedAttemptsCount++
                viewState.showSecurityMessage(
                    getIncorrectAuthMessage(),
                    true
                )
                showFailedActionViewState()
            }
            else -> {
                mSettingsPreferencesManager.setBlockSecurityInputTime(System.currentTimeMillis())
                startBlockSecurityInputTimer(AppConstants.BLOCK_SECURITY_INPUT_DELAY)
                viewState.lockSecurityInputViews()
            }
        }
    }

    private fun isSuccessfulUserInputCode(inputCode: String): Boolean {
        val userPincode = mSettingsPreferencesManager.getUserPincodeValue()
        val userPatternCode = mSettingsPreferencesManager.getUserPatternValue()
        val codeForCheck = if (mIsPincodeAuthMethod) userPincode else userPatternCode
        return inputCode == codeForCheck
    }

    private fun getIncorrectAuthMessage() =
        if (mIsPincodeAuthMethod) {
            "Incorrect pincode. Please, try again"
        } else {
            "Incorrect graph pattern. Please, try again"
        }

    private fun showFailedActionViewState() {
        if (mIsPincodeAuthMethod) {
            viewState.showFailedPincodeAuthAction()
        } else {
            viewState.showFailedPatternAuthAction()
        }
    }

    private fun startBlockSecurityInputTimer(timeForBlock: Long) {
        viewState.showSecurityMessage("Security input blocked. Try after after: $timeForBlock")
        val startedStep = timeForBlock - 1
        Observable.interval(
            AppConstants.BLOCK_SECURITY_INTERVAL,
            TimeUnit.SECONDS,
            Schedulers.computation()
        )
            .take(timeForBlock, TimeUnit.SECONDS)
            .map { leftValue -> startedStep - leftValue }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { tickValue ->
                    viewState.showSecurityMessage("Security input blocked. Try after after: $tickValue")
                },
                { throwable -> Log.d("ded", throwable.message) },
                {
                    resetAttemptsAndUnlockSecurityInput()
                }
            )
            .let(this::bindDisposable)
    }

    private fun resetAttemptsAndUnlockSecurityInput() {
        mFailedAttemptsCount = 0
        viewState.showSecurityMessage("Verify your identity")
        viewState.unlockSecurityInputViews()
    }
}