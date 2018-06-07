package com.ecosnap.Controller.fbDatabase

import com.ecosnap.Model.UserProfile
import com.google.firebase.database.FirebaseDatabase

fun insertNewUserIntoDatabase(db: FirebaseDatabase, firstName: String, lastName: String,
                              email: String, userID: String, descr: String, imgpath: String) {
    val user = UserProfile(firstName, lastName, email)
    if (descr == "") {
        user.descr = "Recycling newbie here! It’s about time that I started to care."
    } else {
        user.descr = descr
    }
    user.imgpath = imgpath

    val ref = db.getReference("users")
    ref.child(userID).child("profile").setValue(user)
}