package com.ecosnap.Model

import java.io.Serializable

class History (val data: Array<DateHistory>) :Serializable

class DateHistory(val label: String, val historyList: Array<HistoryItem>) :Serializable

class HistoryItem (val name: String, val src: Int, val percentage: String, val isRecyclable: Int) :Serializable



