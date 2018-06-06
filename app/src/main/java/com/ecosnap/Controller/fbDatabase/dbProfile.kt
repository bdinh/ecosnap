//package com.ecosnap.Controller.fbDatabase
//
//import android.util.Log
//import com.ecosnap.Model.UserProfile
//import com.google.firebase.database.*
//
//fun getProfileData(ref: DatabaseReference, profile: UserProfile) {
//    val profileListener = object : ValueEventListener {
//        override fun onDataChange(dataSnapshot: DataSnapshot) {
//            val updatedProfile = dataSnapshot.getValue(UserProfile::class.java) as UserProfile
//            profile = updatedProfile
//        }
//
//        override fun onCancelled(p0: DatabaseError) {
//            Log.i("ECOSNAP FIREBASE", "firebase database error: " + p0.toString())
//        }
//    }
//}