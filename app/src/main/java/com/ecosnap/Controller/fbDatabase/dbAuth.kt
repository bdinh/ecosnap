package com.ecosnap.Controller.fbDatabase

import com.ecosnap.Model.UserProfile
import com.google.firebase.database.FirebaseDatabase

fun insertNewUserIntoDatabase(db: FirebaseDatabase, firstName: String, lastName: String, email: String, userID: String) {
    val user = UserProfile(firstName, lastName, email)

    val ref = db.getReference("users")
    ref.child(userID).child("profile").setValue(user)
}