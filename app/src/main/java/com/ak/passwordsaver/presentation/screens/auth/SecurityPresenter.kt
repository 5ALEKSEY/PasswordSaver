package com.ak.passwordsaver.presentation.screens.auth

import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.model.preferences.SettingsPreferencesManager
import com.ak.passwordsaver.presentation.base.BasePSPresenter
import com.arellomobile.mvp.InjectViewState
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
    lateinit var mSettingsPreferencesManager: SettingsPreferencesManager

    var mAuthActionType = AUTH_SECURITY_ACTION_TYPE
    var mIsPincodeAuthMethod = true

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
                viewState.showSecurityMessage("Verify your identity")
                viewState.switchAuthMethod(mIsPincodeAuthMethod)
                if (mSettingsPreferencesManager.isPatternEnabled()) {
                    viewState.unlockSwitchAuthMethod()
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
            viewState.showSecurityMessage("Nope, not that :)")
        }
    }

    private fun handleAddNewSecurityUserInput(inputCode: String) {
        when {
            inputCode.length < MIN_CODE_LENGTH && mAuthActionType == ADD_PATTERN_SECURITY_ACTION_TYPE -> {
                viewState.showSecurityMessage("Why is it so simple? Please, add more lines :)")
            }
            mAddSecurityConfirmCode.isEmpty() -> {
                viewState.showSecurityMessage(getMessageForConfirmCode())
                mAddSecurityConfirmCode = inputCode
            }
            mAddSecurityConfirmCode.equals(inputCode, true) -> {
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
            }
            else -> {
                // TODO: start block timer
                viewState.showSecurityMessage("Too many attempts. Please, try later")
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
}