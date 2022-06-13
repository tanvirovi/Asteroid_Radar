package com.tanvir.asteroidradar

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class GetDate {

    val startDate = getCurrentDate()
    val endDate = getCalculatedDate(startDate, Constants.API_QUERY_DATE_FORMAT, 7)


    @SuppressLint("WeekBasedYear")
    private fun getCurrentDate(): String {
        val cal = Calendar.getInstance()
        val currentTime = cal.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(currentTime)
    }

    private fun getCalculatedDate(date: String, dateFormat: String, days: Int): String {
        val cal = Calendar.getInstance()
        val s = SimpleDateFormat(dateFormat)
        if (date.isNotEmpty()) {
            cal.time = s.parse(date) as Date
        }
        cal.add(Calendar.DAY_OF_YEAR, days)
        return s.format(Date(cal.timeInMillis))
    }

}