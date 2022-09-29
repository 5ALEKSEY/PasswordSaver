package com.ak.core_repo_impl

import com.ak.core_repo_api.intefaces.IDateAndTimeManager
import javax.inject.Inject

class DateAndTimeManagerImpl @Inject constructor() : IDateAndTimeManager {

    override fun getCurrentTimeInMillis() = System.currentTimeMillis()
}