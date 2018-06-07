package com.ecosnap.Controller

import com.ecosnap.Model.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

fun updateProfileChartData (data: MutableList<DateHistory>): ProfileChartData {
    val currDate = getFormattedDate(Date(), "db")

    val pFD = ProfileChartData()
    pFD.dayData = updateProfileDayData(data, currDate)
    pFD.weekData = updateProfileWeekData(data)
    pFD.monthData = updateProfileMonthData(data)
    return pFD
}

fun updateProfileDayData (data: MutableList<DateHistory>, date: String): DayChartData {
    val dayData = DayChartData()
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
    val weekData = WeekChartData()
    var date = calendar.time

    while (dayCounter > -7) {
        val dbDate = getFormattedDate(date, "db")
        val chartDate = getFormattedDate(date, "week")
        val dayData = updateProfileDayData(data, dbDate)
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

fun updateProfileMonthData (data: MutableList<DateHistory>): MonthChartData {
    val monthData = MonthChartData()
    var dayCounter = 0
    var calendar = Calendar.getInstance()
    var date = calendar.time
    var dbDate = getFormattedDate(date, "db")
    val currMonth = dbDate.substring(4, 6)

    while (currMonth == dbDate.substring(4, 6)) {
        val chartDate = getFormattedDate(date, "month")
        val dayData = updateProfileDayData(data, dbDate)
        dayData.date = chartDate
        monthData.monthData.add(dayData)
        dayCounter--
        calendar.add(Calendar.DAY_OF_YEAR, dayCounter)
        date = calendar.time
        dbDate = getFormattedDate(date, "db")
        calendar = Calendar.getInstance()
    }
    return monthData
}

fun getFormattedDate (date: Date, type: String): String {
    when (type) {
        "db" -> { return SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(date) }
        "week" -> { return SimpleDateFormat("MMM d", Locale.getDefault()).format(date) }
        "month" -> { return SimpleDateFormat("MM/dd", Locale.getDefault()).format(date) }
    }
    return ""
}

fun getChartString (data: ArrayList<DayChartData>) : String {
    var result = ""
    for (i in data.size - 1 downTo 1) {
        result += " ['" + data[i].date + "', " + data[i].dayR + ", " + data[i].dayNR + "],"
    }
    result += " ['" + data[0].date + "', " + data[0].dayR + ", " + data[0].dayNR + "]"
    return result
}