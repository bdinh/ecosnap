package com.ecosnap.Model

import java.io.Serializable

class dbHistoryItem (
        val type: String = "",
        val confidence: Float = 0.00F,
        val datetime: String = "",
        val imgPath: String = ""
) : Serializable