package com.ecosnap.Controller

import android.util.Log
import com.ecosnap.Model.DateHistory
import com.ecosnap.Model.DayChartData
import com.ecosnap.Model.ProfileChartData
import java.text.SimpleDateFormat
import java.util.*

fun updateProfileChartData (data: MutableList<DateHistory>): ProfileChartData {
    var pFD = ProfileChartData()
    pFD.dayData = updateProfileDayData(data)

    return pFD
}

fun updateProfileDayData (data: MutableList<DateHistory>): DayChartData {
    var dayData = DayChartData()
    val currDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
    for (dh in data) {
        if (dh.label == currDate) {
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