package com.ecosnap.model

import java.io.Serializable

class History (val data: MutableList<DateHistory>) :Serializable

class DateHistory(val label: String = "", val historyList: MutableList<dbHistoryItem> = mutableListOf()) :Serializable

class HistoryItem (val name: String, val src: Int, val percentage: String, val isRecyclable: Int) :Serializable



