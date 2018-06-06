package com.ecosnap.Controller.fbDatabase

import com.ecosnap.Model.dbHistoryItem
import com.google.firebase.database.DatabaseReference

fun insertHistoryItem (ref: DatabaseReference, item: dbHistoryItem) {
    ref.setValue(item)
}