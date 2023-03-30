package com.example.feature_backup_impl.timeneautifier

import android.content.Context
import android.text.format.DateUtils
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.feature_backup_impl.R
import java.util.Calendar
import javax.inject.Inject

class TimeBeautifierImpl @Inject constructor(
    private val appContext: Context,
    private val resourceManager: IResourceManager,
) : ITimeBeautifier {

    override suspend fun beautifyTimestamp(time: Long): String = when {
        isToday(time) -> {
            "${resourceManager.getString(R.string.date_texts_today)}, ${time.toHoursAndMinutes()}"
        }
        isYesterday(time) -> {
            "${resourceManager.getString(R.string.date_texts_yesterday)}, ${time.toHoursAndMinutes()}"
        }
        isThisWeek(time) -> {
            "${time.toDayOfWeek()}, ${time.toHoursAndMinutes()}"
        }
        else -> time.toSimpleDate()
    }

    private fun isToday(time: Long) = DateUtils.isToday(time)

    private fun isYesterday(time: Long) = isToday(time + DateUtils.DAY_IN_MILLIS)

    private fun isThisWeek(time: Long): Boolean {
        val todayCalendar = Calendar.getInstance()
        val timeCalendar = Calendar.getInstance().apply { timeInMillis = time }
        return todayCalendar.get(Calendar.WEEK_OF_YEAR) == timeCalendar.get(Calendar.WEEK_OF_YEAR)
    }

    private fun Long.toHoursAndMinutes() = DateUtils.formatDateTime(
        appContext,
        this,
        DateUtils.FORMAT_SHOW_TIME,
    )

    private fun Long.toDayOfWeek() = DateUtils.formatDateTime(
        appContext,
        this,
        DateUtils.FORMAT_SHOW_WEEKDAY,
    )

    private fun Long.toSimpleDate() = DateUtils.formatDateTime(
        appContext,
        this,
        DateUtils.FORMAT_SHOW_DATE,
    )
}