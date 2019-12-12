package com.ak.passwordsaver.presentation.base.managers.auth

import com.ak.passwordsaver.presentation.base.managers.auth.AppLockStateConstants.FIVE_MINUTES_STATE_DELAY
import com.ak.passwordsaver.presentation.base.managers.auth.AppLockStateConstants.FIVE_MINUTES_STATE_ID
import com.ak.passwordsaver.presentation.base.managers.auth.AppLockStateConstants.IMMEDIATELY_STATE_DELAY
import com.ak.passwordsaver.presentation.base.managers.auth.AppLockStateConstants.IMMEDIATELY_STATE_ID
import com.ak.passwordsaver.presentation.base.managers.auth.AppLockStateConstants.ONE_MINUTE_STATE_DELAY
import com.ak.passwordsaver.presentation.base.managers.auth.AppLockStateConstants.ONE_MINUTE_STATE_ID

object AppLockStateConstants {
    const val IMMEDIATELY_STATE_ID = 0
    const val ONE_MINUTE_STATE_ID = 1
    const val FIVE_MINUTES_STATE_ID = 2

    const val IMMEDIATELY_STATE_DELAY = 0L
    const val ONE_MINUTE_STATE_DELAY = (1 * 60 * 1000).toLong()
    const val FIVE_MINUTES_STATE_DELAY = (5 * 60 * 1000).toLong()
}

sealed class AppLockState(val lockStateId: Int) {
    abstract fun getLockStateDelayInMillis(): Long
}

class ImmediatelyLockState : AppLockState(IMMEDIATELY_STATE_ID) {
    override fun getLockStateDelayInMillis() = IMMEDIATELY_STATE_DELAY
}

class OneMinuteLockState : AppLockState(ONE_MINUTE_STATE_ID) {
    override fun getLockStateDelayInMillis() = ONE_MINUTE_STATE_DELAY
}

class FiveMinutesLockState : AppLockState(FIVE_MINUTES_STATE_ID) {
    override fun getLockStateDelayInMillis() = FIVE_MINUTES_STATE_DELAY
}

object AppLockStateConverter {
    fun convertFromLockStateId(lockStateId: Int) =
        when (lockStateId) {
            ONE_MINUTE_STATE_ID -> OneMinuteLockState()
            FIVE_MINUTES_STATE_ID -> FiveMinutesLockState()
            else -> ImmediatelyLockState()
        }
}