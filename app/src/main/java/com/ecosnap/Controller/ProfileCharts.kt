package com.ecosnap.Controller

import android.util.Log
import com.ecosnap.Model.DateHistory
import com.ecosnap.Model.DayChartData
import com.ecosnap.Model.ProfileChartData
import com.ecosnap.Model.WeekChartData
import java.text.SimpleDateFormat
import java.util.*

fun updateProfileChartData (data: MutableList<DateHistory>): ProfileChartData {
    val currDate = getFormattedDate(Date(), "db")

    var pFD = ProfileChartData()
    pFD.dayData = updateProfileDayData(data, currDate)
    pFD.weekData = updateProfileWeekData(data)
    return pFD
}

fun updateProfileDayData (data: MutableList<DateHistory>, date: String): DayChartData {
    var dayData = DayChartData()
    for (dh in data) {
        if (dh.label == date) {
            for (item in dh.historyList) {
                when (item.type) {
                    "recyclable" -> dayData.dayR++
                    "nonrecyclable" -> dayData.dayNR++
                }
            }
            break
        }
    }
    return dayData
}

fun updateProfileWeekData (data: MutableList<DateHistory>): WeekChartData {
    var calendar = Calendar.getInstance()
    var dayCounter = 0
    var weekData = WeekChartData()
    var date = calendar.time

    while (dayCounter > -7) {
        val dbDate = getFormattedDate(date, "db")
        val chartDate = getFormattedDate(date, "chart")
        var dayData = updateProfileDayData(data, dbDate)
        when (dayCounter) {
            0 ->    { dayData.date = "Today" }
            -1 ->    { dayData.date = "Yesterday" }
            else -> { dayData.date = chartDate }
        }
        weekData.weekData.add(dayData)
        dayCounter--
        calendar.add(Calendar.DAY_OF_YEAR, dayCounter)
        date = calendar.time
        calendar = Calendar.getInstance()
    }
    return weekData
}

fun getFormattedDate (date: Date, type: String): String {
    when (type) {
        "db" -> { return SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(date) }
        else -> { return SimpleDateFormat("MMM d", Locale.getDefault()).format(date) }
    }
}