package com.ecosnap.Model

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
}

class WeekChartData () : Serializable {
    var weekD1R: Int = 0
    var weekD1NR: Int = 0
    var weekD2R: Int = 0
    var weekD2NR: Int = 0
    var weekD3R: Int = 0
    var weekD3NR: Int = 0
    var weekD4R: Int = 0
    var weekD4NR: Int = 0
    var weekD5R: Int = 0
    var weekD5NR: Int = 0
    var weekD6R: Int = 0
    var weekD6NR: Int = 0
    var weekD7R: Int = 0
    var weekD7NR: Int = 0
}

class MonthChartData () : Serializable {

}