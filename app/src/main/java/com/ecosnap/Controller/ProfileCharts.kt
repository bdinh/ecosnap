package com.ecosnap.Controller

import com.ecosnap.Model.DateHistory
import com.ecosnap.Model.ProfileChartData
import java.text.SimpleDateFormat
import java.util.*

fun updateProfileDayData (data: MutableList<DateHistory>): ProfileChartData {
    val currDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
    for (dh in data) {
        if (dh.label == currDate) {
            for (item in dh.historyList) {

            }
        }
    }
}