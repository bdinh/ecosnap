package com.ecosnap.Controller.fbDatabase

import com.ecosnap.Model.dbHistoryItem
import com.google.firebase.database.FirebaseDatabase

fun insertHistoryItem (userID: String, item: dbHistoryItem) {
    val db = FirebaseDatabase.getInstance()
    val ref = db.getReference("users").child(userID).child("data")
    val key = ref.push().key as String
    ref.child(key).setValue(item)
}