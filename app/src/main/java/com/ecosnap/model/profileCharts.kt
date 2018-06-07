package com.ecosnap.model

import java.io.Serializable

class ProfileChartData () : Serializable {
    var dayData = DayChartData()
    var weekData = WeekChartData()
    var weekChartString = ""
    var monthData = MonthChartData()
    var monthChartString = ""
}

class DayChartData () : Serializable {
    var dayR: Int = 0
    var dayNR: Int = 0
    var date: String = ""
}

class WeekChartData () : Serializable {
    var weekData = arrayListOf<DayChartData>()
}

class MonthChartData () : Serializable {
    var monthData = arrayListOf<DayChartData>()
}