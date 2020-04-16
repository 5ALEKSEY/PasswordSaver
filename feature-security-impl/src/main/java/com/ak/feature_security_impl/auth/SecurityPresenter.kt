package com.ak.feature_security_impl.auth

import android.util.Log
import com.ak.base.constants.AppConstants
import com.ak.base.presenter.BasePSPresenter
import com.ak.core_repo_api.intefaces.ISettingsPreferencesManager
import com.ak.feature_security_api.interfaces.IPSAuthManager.RunActionType.ADD_PATTERN_SECURITY_ACTION_TYPE
import com.ak.feature_security_api.interfaces.IPSAuthManager.RunActionType.ADD_PINCODE_SECURITY_ACTION_TYPE
import com.ak.feature_security_api.interfaces.IPSAuthManager.RunActionType.AUTH_SECURITY_ACTION_TYPE
import com.ak.feature_security_api.interfaces.IPSAuthManager.RunActionType.CHANGE_PATTERN_SECURITY_ACTION_TYPE
import com.ak.feature_security_api.interfaces.IPSAuthManager.RunActionType.CHANGE_PINCODE_SECURITY_ACTION_TYPE
import com.ak.feature_security_impl.di.FeatureSecurityComponent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@InjectViewState
class SecurityPresenter @Inject constructor(
    private val settingsPreferencesManager: ISettingsPreferencesManager
) : BasePSPresenter<ISecurityView>() {

    companion object {
        private const val MAX_FAILED_ATTEMPTS_COUNT = 5
        private const val MIN_CODE_LENGTH = 5
    }

    var authActionType = AUTH_SECURITY_ACTION_TYPE
    private var isPincodeAuthMethod = true

    private var failedAttemptsCount = 0
    private var addSecurityConfirmCode = ""

    init {
        FeatureSecurityComponent.get()
            .provideSecurityScreenComponent()
            .injectSecurityPresenter(this)
    }

    fun onSecurityInputTypeChangeClicked() {
        isPincodeAuthMethod = !isPincodeAuthMethod
        viewState.switchAuthMethod(isPincodeAuthMethod, true)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        initSecurityAuthState()
    }

    private fun initSecurityAuthState() {
        viewState.lockSwitchAuthMethod()
        when (authActionType) {
            AUTH_SECURITY_ACTION_TYPE -> {
                viewState.switchAuthMethod(isPincodeAuthMethod)
                if (settingsPreferencesManager.isPatternEnabled()) {
                    viewState.unlockSwitchAuthMethod()
                }
                val blockSecurityInputTime = settingsPreferencesManager.getBlockSecurityInputTime()
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
                isPincodeAuthMethod = false
                viewState.switchAuthMethod(false)
            }
            CHANGE_PINCODE_SECURITY_ACTION_TYPE -> {
                viewState.showSecurityMessage("Enter your previous pincode")
                viewState.switchAuthMethod(true)
            }
            CHANGE_PATTERN_SECURITY_ACTION_TYPE -> {
                viewState.showSecurityMessage("Enter your previous graph pattern")
                isPincodeAuthMethod = false
                viewState.switchAuthMethod(false)
            }
        }
    }

    fun onUserAuthFinished(inputCode: String) {
        when {
            authActionType == AUTH_SECURITY_ACTION_TYPE -> handleAuthUserInput(inputCode)
            isAddNewSecurityAuthActionType() -> handleAddNewSecurityUserInput(inputCode)
            isChangeSecurityAuthActionType() -> handleChangeSecurityUserInput(inputCode)
        }
    }

    private fun isAddNewSecurityAuthActionType() =
        authActionType == ADD_PINCODE_SECURITY_ACTION_TYPE
                || authActionType == ADD_PATTERN_SECURITY_ACTION_TYPE

    private fun isChangeSecurityAuthActionType() =
        authActionType == CHANGE_PINCODE_SECURITY_ACTION_TYPE
                || authActionType == CHANGE_PATTERN_SECURITY_ACTION_TYPE

    private fun handleChangeSecurityUserInput(inputCode: String) {
        if (isSuccessfulUserInputCode(inputCode)) {
            authActionType = when (authActionType) {
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
            addSecurityConfirmCode.isNotEmpty() -> {
                if (addSecurityConfirmCode.equals(inputCode, true)) {
                    viewState.hideSecurityMessage()
                    when (authActionType) {
                        ADD_PINCODE_SECURITY_ACTION_TYPE -> {
                            settingsPreferencesManager.setPincodeEnableState(true)
                            settingsPreferencesManager.setUserPincodeValue(addSecurityConfirmCode)
                        }
                        ADD_PATTERN_SECURITY_ACTION_TYPE -> {
                            settingsPreferencesManager.setPatternEnableState(true)
                            settingsPreferencesManager.setUserPatternValue(addSecurityConfirmCode)
                        }
                    }
                    viewState.sendAuthActionResult(true)
                } else {
                    viewState.showSecurityMessage(getMessageForConfirmCode(), true)
                    showFailedActionViewState()
                }
            }
            inputCode.length < MIN_CODE_LENGTH && authActionType == ADD_PATTERN_SECURITY_ACTION_TYPE -> {
                viewState.showSecurityMessage(
                    "Why is it so simple? Please, add more lines :)",
                    true
                )
                showFailedActionViewState()
            }
            addSecurityConfirmCode.isEmpty() -> {
                viewState.showSecurityMessage(getMessageForConfirmCode())
                addSecurityConfirmCode = inputCode
            }
        }
    }

    private fun getMessageForConfirmCode() =
        if (isPincodeAuthMethod) {
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
            failedAttemptsCount != MAX_FAILED_ATTEMPTS_COUNT -> {
                failedAttemptsCount++
                viewState.showSecurityMessage(
                    getIncorrectAuthMessage(),
                    true
                )
                showFailedActionViewState()
            }
            else -> {
                settingsPreferencesManager.setBlockSecurityInputTime(System.currentTimeMillis())
                startBlockSecurityInputTimer(AppConstants.BLOCK_SECURITY_INPUT_DELAY)
                viewState.lockSecurityInputViews()
            }
        }
    }

    private fun isSuccessfulUserInputCode(inputCode: String): Boolean {
        val userPincode = settingsPreferencesManager.getUserPincodeValue()
        val userPatternCode = settingsPreferencesManager.getUserPatternValue()
        val codeForCheck = if (isPincodeAuthMethod) userPincode else userPatternCode
        return inputCode == codeForCheck
    }

    private fun getIncorrectAuthMessage() =
        if (isPincodeAuthMethod) {
            "Incorrect pincode. Please, try again"
        } else {
            "Incorrect graph pattern. Please, try again"
        }

    private fun showFailedActionViewState() {
        if (isPincodeAuthMethod) {
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
        failedAttemptsCount = 0
        viewState.showSecurityMessage("Verify your identity")
        viewState.unlockSecurityInputViews()
    }
}