package com.ak.feature_security_impl.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ak.base.constants.AppConstants
import com.ak.base.viewmodel.BasePSViewModel
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.core_repo_api.intefaces.ISettingsPreferencesManager
import com.ak.feature_security_api.interfaces.IAuthCheckerStarter
import com.ak.feature_security_api.interfaces.IPSBiometricManager
import com.ak.feature_security_impl.R
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SecurityViewModel @Inject constructor(
    private val settingsPreferencesManager: ISettingsPreferencesManager,
    private val psBiometricManager: IPSBiometricManager,
    private val resourceManager: IResourceManager
) : BasePSViewModel() {

    companion object {
        private const val MAX_FAILED_ATTEMPTS_COUNT = 5
        private const val MIN_CODE_LENGTH = 5
    }

    var authActionType = IAuthCheckerStarter.AUTH_SECURITY_ACTION_TYPE
    private var isPincodeAuthMethod = true

    private var failedAttemptsCount = 0
    private var addSecurityConfirmCode = ""

    private var isInputBlocked = false

    private val switchAuthMethodLiveData = MutableLiveData<Pair<Boolean, Boolean>>()
    private val showSecurityMessageLiveData = MutableLiveData<Pair<String, Boolean>>()
    private val hideSecurityMessageLiveData = MutableLiveData<Unit?>()
    private val failedPincodeAuthActionLiveData = MutableLiveData<Unit?>()
    private val failedPatternAuthActionLiveData = MutableLiveData<Unit?>()
    private val securityInputLockStateLiveData = MutableLiveData<Boolean>()
    private val switchAuthMethodLockStateLiveData = MutableLiveData<Boolean>()
    private val biometricAuthVisibilityStateLiveData = MutableLiveData<Boolean>()
    private val biometricAuthLockStateLiveData = MutableLiveData<Boolean>()
    private val biometricAuthFailedAttemptLiveData = MutableLiveData<Unit?>()
    private val authActionResultLiveData = MutableLiveData<Boolean>()

    fun subscribeToSwitchAuthMethodLiveData(): LiveData<Pair<Boolean, Boolean>> = switchAuthMethodLiveData
    fun subscribeToShowSecurityMessageLiveData(): LiveData<Pair<String, Boolean>> = showSecurityMessageLiveData
    fun subscribeToHideSecurityMessageLiveData(): LiveData<Unit?> = hideSecurityMessageLiveData
    fun subscribeToFailedPincodeAuthActionLiveData(): LiveData<Unit?> = failedPincodeAuthActionLiveData
    fun subscribeToFailedPatternAuthActionLiveData(): LiveData<Unit?> = failedPatternAuthActionLiveData
    fun subscribeToSecurityInputLockStateLiveData(): LiveData<Boolean> = securityInputLockStateLiveData
    fun subscribeToSwitchAuthMethodLockStateLiveData(): LiveData<Boolean> = switchAuthMethodLockStateLiveData
    fun subscribeToBiometricAuthVisibilityStateLiveData(): LiveData<Boolean> = biometricAuthVisibilityStateLiveData
    fun subscribeToBiometricAuthLockStateLiveData(): LiveData<Boolean> = biometricAuthLockStateLiveData
    fun subscribeToBiometricAuthFailedAttemptLiveData(): LiveData<Unit?> = biometricAuthFailedAttemptLiveData
    fun subscribeToAuthActionResultLiveData(): LiveData<Boolean> = authActionResultLiveData

    fun onSecurityInputTypeChangeClicked() {
        isPincodeAuthMethod = !isPincodeAuthMethod
        switchAuthMethodLiveData.value = isPincodeAuthMethod to true
    }

    fun startBiometricAuth() {
        val canUseBiometricAuth = canUseBiometricAuth()
        biometricAuthVisibilityStateLiveData.value = canUseBiometricAuth
        if (canUseBiometricAuth) {
            psBiometricManager.startAuth(object : IPSBiometricManager.AuthCallback {
                override fun onSucceeded() {
                    sendSuccessfullyAuthResult()
                }

                override fun onFailedAttempt() {
                    biometricAuthFailedAttemptLiveData.invoke()
                }

                override fun onBiometricLocked() {
                    biometricAuthLockStateLiveData.value = true
                }

                override fun onHelpForUser(helpMessage: String) {
                    showSecurityMessageLiveData.value = helpMessage to false
                }
            })
        }
    }

    fun stopBiometricAuth() {
        if (canUseBiometricAuth()) {
            psBiometricManager.cancelAuth()
        }
    }

    fun initSecurityAuthState() {
        switchAuthMethodLockStateLiveData.value = true
        when (authActionType) {
            IAuthCheckerStarter.AUTH_SECURITY_ACTION_TYPE -> {
                switchAuthMethodLiveData.value = isPincodeAuthMethod to false
                if (settingsPreferencesManager.isPatternEnabled()) {
                    switchAuthMethodLockStateLiveData.value = false
                }
                val blockSecurityInputTime = settingsPreferencesManager.getBlockSecurityInputTime()
                val leftBlockInterval = (System.currentTimeMillis() - blockSecurityInputTime) / 1000
                if (leftBlockInterval > AppConstants.BLOCK_SECURITY_INPUT_DELAY) {
                    resetAttemptsAndUnlockSecurityInput()
                } else {
                    securityInputLockStateLiveData.value = true
                    val timeForBlock = AppConstants.BLOCK_SECURITY_INPUT_DELAY - leftBlockInterval
                    startBlockSecurityInputTimer(timeForBlock)
                }
            }
            IAuthCheckerStarter.ADD_PINCODE_SECURITY_ACTION_TYPE -> {
                showSecurityMessageLiveData.value = resourceManager.getString(R.string.add_new_pincode_message) to false
                switchAuthMethodLiveData.value = true to false
            }
            IAuthCheckerStarter.ADD_PATTERN_SECURITY_ACTION_TYPE -> {
                showSecurityMessageLiveData.value = resourceManager.getString(R.string.add_new_pattern_message) to false
                isPincodeAuthMethod = false
                switchAuthMethodLiveData.value = false to false
            }
            IAuthCheckerStarter.CHANGE_PINCODE_SECURITY_ACTION_TYPE -> {
                val message = resourceManager.getString(
                    R.string.change_old_security_input_message,
                    resourceManager.getString(R.string.security_input_pincode)
                )
                showSecurityMessageLiveData.value = message to false
                switchAuthMethodLiveData.value = true to false
            }
            IAuthCheckerStarter.CHANGE_PATTERN_SECURITY_ACTION_TYPE -> {
                val message = resourceManager.getString(
                    R.string.change_old_security_input_message,
                    resourceManager.getString(R.string.security_input_pattern)
                )
                showSecurityMessageLiveData.value = message to false
                isPincodeAuthMethod = false
                switchAuthMethodLiveData.value = false to false
            }
        }
    }

    fun onUserAuthFinished(inputCode: String) {
        when {
            authActionType == IAuthCheckerStarter.AUTH_SECURITY_ACTION_TYPE -> handleAuthUserInput(inputCode)
            isAddNewSecurityAuthActionType() -> handleAddNewSecurityUserInput(inputCode)
            isChangeSecurityAuthActionType() -> handleChangeSecurityUserInput(inputCode)
        }
    }

    private fun isAddNewSecurityAuthActionType() =
        authActionType == IAuthCheckerStarter.ADD_PINCODE_SECURITY_ACTION_TYPE
            || authActionType == IAuthCheckerStarter.ADD_PATTERN_SECURITY_ACTION_TYPE

    private fun isChangeSecurityAuthActionType() =
        authActionType == IAuthCheckerStarter.CHANGE_PINCODE_SECURITY_ACTION_TYPE
            || authActionType == IAuthCheckerStarter.CHANGE_PATTERN_SECURITY_ACTION_TYPE

    private fun handleChangeSecurityUserInput(inputCode: String) {
        if (isSuccessfulUserInputCode(inputCode)) {
            authActionType = when (authActionType) {
                IAuthCheckerStarter.CHANGE_PINCODE_SECURITY_ACTION_TYPE -> IAuthCheckerStarter.ADD_PINCODE_SECURITY_ACTION_TYPE
                IAuthCheckerStarter.CHANGE_PATTERN_SECURITY_ACTION_TYPE -> IAuthCheckerStarter.ADD_PATTERN_SECURITY_ACTION_TYPE
                else -> IAuthCheckerStarter.ADD_PINCODE_SECURITY_ACTION_TYPE
            }
            initSecurityAuthState()
        } else {
            showSecurityMessageLiveData.value = resourceManager.getString(R.string.change_security_input_error) to true
            showFailedActionViewState()
        }
    }

    private fun handleAddNewSecurityUserInput(inputCode: String) {
        when {
            addSecurityConfirmCode.isNotEmpty() -> {
                if (addSecurityConfirmCode.equals(inputCode, true)) {
                    hideSecurityMessageLiveData.invoke()
                    when (authActionType) {
                        IAuthCheckerStarter.ADD_PINCODE_SECURITY_ACTION_TYPE -> {
                            settingsPreferencesManager.setPincodeEnableState(true)
                            settingsPreferencesManager.setUserPincodeValue(addSecurityConfirmCode)
                        }
                        IAuthCheckerStarter.ADD_PATTERN_SECURITY_ACTION_TYPE -> {
                            settingsPreferencesManager.setPatternEnableState(true)
                            settingsPreferencesManager.setUserPatternValue(addSecurityConfirmCode)
                        }
                    }
                    sendSuccessfullyAuthResult()
                } else {
                    showSecurityMessageLiveData.value = getMessageForConfirmCode() to true
                    showFailedActionViewState()
                }
            }
            inputCode.length < MIN_CODE_LENGTH && authActionType == IAuthCheckerStarter.ADD_PATTERN_SECURITY_ACTION_TYPE -> {
                showSecurityMessageLiveData.value = resourceManager.getString(R.string.pattern_incorrect_lines_count) to true
                showFailedActionViewState()
            }
            addSecurityConfirmCode.isEmpty() -> {
                showSecurityMessageLiveData.value = getMessageForConfirmCode() to false
                addSecurityConfirmCode = inputCode
            }
        }
    }

    private fun getMessageForConfirmCode(): String {
        val securityInputName = if (isPincodeAuthMethod) {
            resourceManager.getString(R.string.security_input_pincode)
        } else {
            resourceManager.getString(R.string.security_input_pattern)
        }

        return resourceManager.getString(R.string.confirm_security_input_message, securityInputName)
    }

    private fun handleAuthUserInput(inputCode: String) {
        when {
            isSuccessfulUserInputCode(inputCode) -> {
                sendSuccessfullyAuthResult()
            }
            failedAttemptsCount != MAX_FAILED_ATTEMPTS_COUNT -> {
                failedAttemptsCount++
                showSecurityMessageLiveData.value = getIncorrectAuthMessage() to true
                showFailedActionViewState()
            }
            else -> {
                settingsPreferencesManager.setBlockSecurityInputTime(System.currentTimeMillis())
                startBlockSecurityInputTimer(AppConstants.BLOCK_SECURITY_INPUT_DELAY)
                securityInputLockStateLiveData.value = true
            }
        }
    }

    private fun sendSuccessfullyAuthResult() {
        hideSecurityMessageLiveData.invoke()
        authActionResultLiveData.value = true
    }

    private fun isSuccessfulUserInputCode(inputCode: String): Boolean {
        val userPincode = settingsPreferencesManager.getUserPincodeValue()
        val userPatternCode = settingsPreferencesManager.getUserPatternValue()
        val codeForCheck = if (isPincodeAuthMethod) userPincode else userPatternCode
        return inputCode == codeForCheck
    }

    private fun getIncorrectAuthMessage(): String {
        val securityInputName = if (isPincodeAuthMethod) {
            resourceManager.getString(R.string.security_input_pincode)
        } else {
            resourceManager.getString(R.string.security_input_pattern)
        }

        return resourceManager.getString(R.string.incorrect_security_input_message, securityInputName)
    }

    private fun showFailedActionViewState() {
        if (isPincodeAuthMethod) {
            failedPincodeAuthActionLiveData.invoke()
        } else {
            failedPatternAuthActionLiveData.invoke()
        }
    }

    private fun startBlockSecurityInputTimer(timeForBlock: Long) {
        showSecurityMessageLiveData.value = resourceManager.getString(R.string.security_input_blocked_message, timeForBlock.toInt()) to false
        val startedStep = timeForBlock - 1
        Observable.interval(
            AppConstants.BLOCK_SECURITY_INTERVAL,
            TimeUnit.SECONDS,
            Schedulers.computation()
        )
            .take(timeForBlock, TimeUnit.SECONDS)
            .map { leftValue -> startedStep - leftValue }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { isInputBlocked = true }
            .subscribe(
                { tickValue ->
                    showSecurityMessageLiveData.value = resourceManager.getString(R.string.security_input_blocked_message, tickValue.toInt()) to false
                },
                { throwable -> Log.d("ded", throwable.message ?: "") },
                {
                    resetAttemptsAndUnlockSecurityInput()
                }
            )
            .let(this::bindDisposable)
    }

    private fun resetAttemptsAndUnlockSecurityInput() {
        failedAttemptsCount = 0
        isInputBlocked = false
        showSecurityMessageLiveData.value = resourceManager.getString(R.string.security_input_start_message) to false
        securityInputLockStateLiveData.value = false
    }

    private fun canUseBiometricAuth() = authActionType == IAuthCheckerStarter.AUTH_SECURITY_ACTION_TYPE
        && psBiometricManager.isBiometricAuthEnabled()
        && !isInputBlocked

    private fun MutableLiveData<Unit?>.invoke() {
        value = null
    }
}