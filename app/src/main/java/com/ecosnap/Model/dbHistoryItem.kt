package com.ecosnap.Model

import android.graphics.Bitmap
import java.io.Serializable

class dbHistoryItem (
        val type: String = "",
        val confidence: Float = 0.00F,
        val datetime: String = "",
        val imgPath: String = ""
) : Serializable

class  UIhistoryItem (
        val type: String,
        val confidence: String,
        val date: String,
        val image: Bitmap
        ) :Serializable